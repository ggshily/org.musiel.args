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
package org.musiel.args.syntax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.musiel.args.ArgumentException;
import org.musiel.args.Option;
import org.musiel.args.syntax.Syntax.SyntaxResult;

public abstract class AbstractParseResult implements SyntaxResult {

	protected final LinkedList< ArgumentException> errors = new LinkedList<>();

	@ Override
	public Collection< ? extends ArgumentException> getErrors() {
		return Collections.unmodifiableCollection( this.errors);
	}

	private final Set< Option> options;
	protected final Map< String, Option> optionDictionary = new TreeMap<>();
	protected List< String> operands = new LinkedList<>();

	protected AbstractParseResult( final Set< Option> options) {
		this.options = options;
		for( final Option option: this.options)
			for( final String name: option.getNames())
				if( this.optionDictionary.containsKey( name))
					throw new IllegalArgumentException( "duplicate name: " + name);
				else
					this.optionDictionary.put( name, option);
	}

	private String getCanonicalName( final String optionName) {
		return this.optionDictionary.containsKey( optionName)? this.optionDictionary.get( optionName).getName(): optionName;
	}

	private final Map< String, List< String>> optionNames = new TreeMap<>();
	private final Map< String, List< String>> optionArguments = new TreeMap<>();

	private List< String> initializeAndGet( final Map< String, List< String>> map, final String optionName) {
		final String canonicalName = this.getCanonicalName( optionName);
		List< String> list = map.get( canonicalName);
		if( list == null)
			map.put( canonicalName, list = new LinkedList<>());
		return list;
	}

	protected void push( final String optionName, final String optionArgument) {
		this.initializeAndGet( this.optionNames, optionName).add( optionName);
		this.initializeAndGet( this.optionArguments, optionName).add( optionArgument);
	}

	@ Override
	public List< String> getNames( final String optionName) {
		return this.optionNames.get( this.getCanonicalName( optionName));
	}

	@ Override
	public List< String> getArguments( final String optionName) {
		return this.optionArguments.get( this.getCanonicalName( optionName));
	}

	@ Override
	public List< String> getOperands() {
		return this.operands;
	}

	protected void build() {
		// freeze lists
		for( final Entry< String, List< String>> entry: this.optionNames.entrySet())
			entry.setValue( Collections.unmodifiableList( new ArrayList<>( entry.getValue())));
		for( final Entry< String, List< String>> entry: this.optionArguments.entrySet())
			entry.setValue( Collections.unmodifiableList( new ArrayList<>( entry.getValue())));
		this.operands = Collections.unmodifiableList( new ArrayList<>( this.operands));

		for( final Option option: this.options) {
			List< String> names = this.optionNames.get( option.getName());
			List< String> arguments = this.optionArguments.get( option.getName());
			if( names == null) {
				this.optionNames.put( option.getName(), names = Collections.< String>emptyList());
				this.optionArguments.put( option.getName(), arguments = Collections.< String>emptyList());
			}

			if( option.isRequired() && names.isEmpty())
				this.errors.add( new MissingOptionException( option.getName()));
			if( !option.isRepeatable() && names.size() > 1)
				this.errors.add( new TooManyOccurrenceException( names.get( 1), names));
			final Iterator< String> nameIterator = names.iterator();
			final Iterator< String> argumentIterator = arguments.iterator();
			while( nameIterator.hasNext()) {
				final String name = nameIterator.next();
				final String argument = argumentIterator.next();
				if( !option.getArgumentPolicy().isAccepted() && argument != null)
					this.errors.add( new UnexpectedArgumentException( name));
				if( option.getArgumentPolicy().isRequired() && argument == null)
					this.errors.add( new ArgumentRequiredException( name));
			}
		}
	}
}
