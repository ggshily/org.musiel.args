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

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * An abstract implementation of {@link Option}.
 * 
 * @author Bagana
 */
abstract class AbstractOption implements Option {

	private final String name;
	private final Set< String> names = new LinkedHashSet<>();

	private final boolean required;
	private final boolean repeatable;
	private final ArgumentPolicy argumentPolicy;

	/**
	 * Construct an instance.
	 * 
	 * <ul>
	 * <li>{@code primaryName} must not be <code>null</code></li>
	 * <li>{@code additionalNames} must not contain <code>null</code></li>
	 * <li>The first element of {@link #getNames()} is always {@code primaryName}, and the rest are stored in the same order the iterator
	 * of {@code additionalNames} yields.</li>
	 * <li>Duplicate names, if any, are simply skipped.</li>
	 * </ul>
	 * 
	 * @param primaryName
	 * @param additionalNames
	 * @param required
	 * @param repeatable
	 * @param argumentPolicy
	 * @param description
	 * @param argumentName
	 */
	AbstractOption( final String primaryName, final Collection< String> additionalNames, final boolean required, final boolean repeatable,
			final ArgumentPolicy argumentPolicy) {
		this.name = primaryName;
		this.names.add( this.name);
		if( additionalNames != null)
			this.names.addAll( additionalNames);

		this.required = required;
		this.repeatable = repeatable;
		this.argumentPolicy = argumentPolicy;

		if( this.names.contains( null) || argumentPolicy == null)
			throw new NullPointerException();
	}

	AbstractOption( final Option option) {
		this( option.getName(), option.getNames(), option.isRequired(), option.isRepeatable(), option.getArgumentPolicy());
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
}
