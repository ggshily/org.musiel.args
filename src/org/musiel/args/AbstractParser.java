/*
 * Copyright 2014 Bagana <bagana@musiel.org>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You 
 * may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.musiel.args;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import org.musiel.args.operand.OperandException;
import org.musiel.args.operand.OperandPattern;
import org.musiel.args.syntax.Syntax;
import org.musiel.args.syntax.Syntax.SyntaxResult;

/**
 * An abstract implementation of {@link Parser}.
 * 
 * @author Bagana
 * 
 * @param <RESULT>
 */
public abstract class AbstractParser< RESULT extends Result< ?>> implements Parser< RESULT> {

	private final Syntax syntax;

	protected AbstractParser( final Syntax syntax) {
		this.syntax = syntax;
		if( this.syntax == null)
			throw new NullPointerException();
	}

	private String resourceBundleBase = null;

	protected void setResourceBundleBase( final String base) {
		this.resourceBundleBase = base;
	}

	private String getResource( final Locale locale, final String key) {
		try {
			return this.resourceBundleBase == null? null: ResourceBundle.getBundle( this.resourceBundleBase, locale).getString( key);
		} catch( final MissingResourceException exception) {
			return null;
		}
	}

	private String getResource( final Locale locale, final Collection< String> tryKeys, final String keyPrefix, final String keySuffix) {
		if( this.resourceBundleBase == null)
			return null;
		try {
			final ResourceBundle bundle = ResourceBundle.getBundle( this.resourceBundleBase, locale);
			for( final String key: tryKeys)
				try {
					return bundle.getString( keyPrefix + key + keySuffix);
				} catch( final MissingResourceException exception) {
				}
		} catch( final MissingResourceException exception) {
		}
		return null;
	}

	private String description = null;

	@ Override
	public String getDescription() {
		return this.getDescription( Locale.getDefault());
	}

	@ Override
	public String getDescription( final Locale locale) {
		return this.description != null? this.description: this.getResource( locale, "description");
	}

	protected void setDescription( final String description) {
		this.description = description;
	}

	private final Map< String, Option> optionDictionary = new TreeMap<>();
	private final Set< Option> options = new LinkedHashSet<>();

	@ Override
	public Collection< ? extends Option> getOptions() {
		return Collections.unmodifiableCollection( this.options);
	}

	@ Override
	public Option getOption( final String name) {
		return this.optionDictionary.get( name);
	}

	protected Option addOption( final String primaryName, final String[] additionalNames, final boolean required,
			final boolean repeatable, final ArgumentPolicy argumentPolicy, final String description, final String argumentName) {
		return this.addOption( primaryName, AbstractParser.arrayToCollection( additionalNames), required, repeatable, argumentPolicy,
				description, argumentName);
	}

	private static Collection< String> arrayToCollection( final String[] array) {
		final Collection< String> collection = new LinkedList<>();
		if( array != null)
			Collections.addAll( collection, array);
		return collection;
	}

	protected Option addOption( final String primaryName, final Collection< String> additionalNames, final boolean required,
			final boolean repeatable, final ArgumentPolicy argumentPolicy, final String description, final String argumentName) {
		final Option option = new AbstractOption( primaryName, additionalNames, required, repeatable, argumentPolicy) {

			@ Override
			public String getDescription( final Locale locale) {
				return description != null? description: AbstractParser.this.getResource( locale, this.getNames(), "option.", ".description");
			}

			@ Override
			public String getArgumentName( final Locale locale) {
				return argumentName != null? argumentName: AbstractParser.this.getResource( locale, this.getNames(), "option.", ".argument");
			}
		};

		this.syntax.validate( option);
		for( final String optionName: option.getNames())
			if( this.optionDictionary.containsKey( optionName))
				throw new IllegalArgumentException( "duplicate option name: " + optionName);

		this.options.add( option);
		for( final String optionName: option.getNames())
			this.optionDictionary.put( optionName, option);

		return option;
	}

	private OperandPattern operandPattern = null;
	private final Map< String, String> operandDescriptions = new TreeMap<>();

	@ Override
	public String getOperandPattern() {
		return this.operandPattern == null? null: this.operandPattern.getPattern();
	}

	@ Override
	public Collection< String> getOperandNames() {
		return this.operandPattern == null? null: this.operandPattern.getNames();
	}

	protected void setOperandPattern( final String operandPattern) {
		this.operandPattern = operandPattern == null? null: this.compilePatternAndCheckAmbiguity( operandPattern);
		this.operandDescriptions.clear();
	}

	@ Override
	public String getOperandDescription( final String operandName) {
		return this.getOperandDescription( operandName, Locale.getDefault());
	}

	@ Override
	public String getOperandDescription( final String operandName, final Locale locale) {
		if( this.operandDescriptions.containsKey( operandName))
			return this.operandDescriptions.get( operandName);
		return this.getResource( locale, "operand." + operandName);
	}

	protected void setOperandDescription( final String operandName, final String operandDescription) {
		if( this.operandPattern == null || !this.getOperandNames().contains( operandName))
			throw new IllegalArgumentException( "operand name " + operandName + " does not exist in the pattern");
		this.operandDescriptions.put( operandName, operandDescription);
	}

	private OperandPattern compilePatternAndCheckAmbiguity( final String operandPattern) {
		final OperandPattern compiled = OperandPattern.compile( operandPattern);
		final String[][] ambiguityExample = compiled.findAmbiguityExample();
		if( ambiguityExample == null)
			return compiled;
		final StringBuilder message =
				new StringBuilder().append( "operand pattern \"").append( operandPattern)
						.append( "\" is ambiguous, for example, if the input length is ").append( ambiguityExample[ 0].length)
						.append( ", there are at least following two interpretation: \"");
		this.append( message, ambiguityExample[ 0]);
		message.append( "\" and \"");
		this.append( message, ambiguityExample[ 0]);
		message.append( "\"");
		throw new IllegalArgumentException( message.toString());
	}

	private void append( final StringBuilder message, final String[] strings) {
		for( int index = 0; index < strings.length; ++index) {
			if( index >= 0)
				message.append( ' ');
			message.append( strings[ index]);
		}
	}

	@ Override
	public RESULT parse( final String[] args, final int offset) {
		if( offset < 0 || offset >= args.length)
			throw new ArrayIndexOutOfBoundsException( offset);
		return this.parse( args, offset, args.length - offset);
	}

	@ Override
	public RESULT parse( final String[] args, final int offset, final int length) {
		if( offset < 0 || offset >= args.length)
			throw new ArrayIndexOutOfBoundsException( offset);
		if( length < 0)
			throw new IllegalArgumentException( String.valueOf( length));
		if( offset + length > args.length)
			throw new ArrayIndexOutOfBoundsException( offset + length);
		return this.parse( Arrays.copyOfRange( args, offset, offset + length));
	}

	@ Override
	public RESULT parse( final String... args) {
		final SyntaxResult syntaxResult = this.syntax.parse( Collections.unmodifiableSet( this.options), args);
		final Collection< ArgumentException> exceptions = new LinkedList< ArgumentException>( syntaxResult.getErrors());
		Map< String, List< String>> operandMap = null;
		if( this.operandPattern != null)
			try {
				operandMap = this.operandPattern.match( syntaxResult.getOperands());
			} catch( final OperandException exception) {
				exceptions.add( exception);
			}

		return this.buildResult( syntaxResult, operandMap != null? operandMap: Collections.< String, List< String>>emptyMap(),
				Collections.unmodifiableCollection( exceptions));
	}

	protected abstract RESULT buildResult( SyntaxResult syntaxResult, Map< String, List< String>> operands,
			Collection< ? extends ArgumentException> exceptions);
}
