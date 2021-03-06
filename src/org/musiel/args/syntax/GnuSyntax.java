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

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.musiel.args.Option;

/**
 * A {@link Syntax} implementation compliant with the GNU <a
 * href="http://www.gnu.org/software/libc/manual/html_node/Argument-Syntax.html">Program Argument Syntax Conventions</a>.
 * 
 * <p>
 * The Conventions page mentioned above may contain some ambiguity, which is clarified here:
 * <ol>
 * <li>When multiple options are included in a single argument, the last one MAY take an argument.</li>
 * <li>An optional option-argument, if supported at all, MUST follow immediately after the option name in the same argument string.</li>
 * <li>The first argument "--" terminates all options, ONLY when it is not an option-argument.</li>
 * <li>If a long option name happens to be the starting part of another, the latter cannot be abbreviated to the former (an exact match
 * takes precedence).</li>
 * </ol>
 * </p>
 * 
 * <p>
 * The GNU implementations, by default, allow options to be specified after some of the operands, this implementation chooses the same
 * default behavior. This means that the default value for {@link #setLateOptionsAllowed(boolean)} is different from its super class
 * {@link PosixSyntax}.
 * </p>
 * 
 * <p>
 * Some programmers MIGHT think option name abbreviation a trouble making feature, it can be disabled by
 * {@link #setAbbreviationAllowed(boolean)}.
 * </p>
 * 
 * <p>
 * {@link #setOptionalArgumentsAllowed(boolean)} also controls whether optional option-arguments for long options are permitted.
 * </p>
 * 
 * @author Bagana
 */
public class GnuSyntax extends PosixSyntax {

	@ Override
	public GnuSyntax setOptionalArgumentsAllowed( final boolean optionalArgumentsAllowed) {
		super.setOptionalArgumentsAllowed( optionalArgumentsAllowed);
		return this;
	}

	@ Override
	public GnuSyntax setJointArgumentsAllowed( final boolean jointArgumentAllowed) {
		super.setJointArgumentsAllowed( jointArgumentAllowed);
		return this;
	}

	@ Override
	public GnuSyntax setLateOptionsAllowed( final boolean lateOptionsAllowed) {
		super.setLateOptionsAllowed( lateOptionsAllowed);
		return this;
	}

	private boolean abbreviationAllowed = true;

	public boolean isAbbreviationAllowed() {
		return this.abbreviationAllowed;
	}

	public GnuSyntax setAbbreviationAllowed( final boolean abbreviationAllowed) {
		this.abbreviationAllowed = abbreviationAllowed;
		return this;
	}

	public GnuSyntax() {
		this.setLateOptionsAllowed( true);
	}

	@ Override
	protected void validateName( final String name) throws IllegalArgumentException {
		if( !this.isValidName( name))
			throw new IllegalArgumentException( "\"" + name + "\" is not a valid GNU option name");
	}

	private static final Pattern OPTION_NAME_PATTERN = Pattern.compile( "^\\-(?:[A-Za-z0-9]|\\-[A-Za-z0-9\\-]+)$");

	private boolean isValidName( final String name) {
		return GnuSyntax.OPTION_NAME_PATTERN.matcher( name).find();
	}

	@ Override
	protected PosixMachine newMachine( final Set< Option> options) {
		return new GnuMachine( options);
	}

	protected class GnuMachine extends PosixMachine {

		public GnuMachine( final Set< Option> options) {
			super( options);
		}

		@ Override
		protected void handleOption( final String arg) {
			if( arg.startsWith( "--"))
				this.handleLongOption( arg);
			else
				super.handleOption( arg);
		}

		private void handleLongOption( final String arg) {
			final int equalPos = arg.indexOf( '=');
			String optionName = equalPos < 0? arg: arg.substring( 0, equalPos); // "--" is possible here
			final String argument = equalPos < 0? null: arg.substring( equalPos + 1);
			if( !this.operands.isEmpty() && !GnuSyntax.this.isLateOptionsAllowed())
				this.errors.add( new LateOptionException( optionName));

			Option option = this.optionDictionary.get( optionName);
			if( option == null)
				if( !GnuSyntax.this.isAbbreviationAllowed())
					this.errors.add( new UnknownOptionException( optionName));
				else
					option = this.optionDictionary.get( optionName = this.findAbbreviatedName( optionName));

			if( argument != null || option != null && !option.getArgumentPolicy().isRequired())
				this.push( optionName, argument);
			else {
				this.openOptionName = optionName;
				this.openOption = option;
			}
		}

		private String findAbbreviatedName( final String optionName) {
			final Set< String> candidates = new TreeSet<>();
			for( final String candidate: this.optionDictionary.keySet())
				if( candidate.startsWith( optionName))
					candidates.add( candidate);
			if( candidates.isEmpty()) {
				this.errors.add( new UnknownOptionException( optionName));
				return optionName;
			}
			if( candidates.size() > 1) {
				this.errors.add( new AmbiguousOptionNameException( optionName));
				return optionName;
			}
			return candidates.iterator().next();
		}
	}
}
