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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public class TestOption implements Option {

	private final String name;
	private final Set< String> names = new LinkedHashSet<>();
	private final boolean required;
	private final boolean repeatable;
	private final ArgumentPolicy argumentPolicy;

	public TestOption( final String... names) {
		this( false, false, ArgumentPolicy.NONE, names);
	}

	public TestOption( final boolean required, final boolean repeatable, final ArgumentPolicy argumentPolicy, final String... names) {
		this.name = names[ 0];
		this.names.add( this.name);
		Collections.addAll( this.names, names);
		this.required = required;
		this.repeatable = repeatable;
		this.argumentPolicy = argumentPolicy;
		if( this.names.contains( null) || argumentPolicy == null)
			throw new NullPointerException();
	}

	@ Override
	public String getName() {
		return this.name;
	}

	@ Override
	public Set< String> getNames() {
		return this.names;
	}

	@ Override
	public boolean isRequired() {
		return this.required;
	}

	@ Override
	public boolean isRepeatable() {
		return this.repeatable;
	}

	@ Override
	public ArgumentPolicy getArgumentPolicy() {
		return this.argumentPolicy;
	}

	@ Override
	public String getDescription() {
		return this.getDescription( Locale.getDefault());
	}

	@ Override
	public String getArgumentName() {
		return this.getArgumentName( Locale.getDefault());
	}

	@ Override
	public String getDescription( final Locale locale) {
		return null;
	}

	@ Override
	public String getArgumentName( final Locale locale) {
		return null;
	}
}
