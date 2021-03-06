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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A collection of {@link ArgumentException}. Used when throwing multiple {@link ArgumentException} at once is needed.
 * 
 * @author Bagana
 */
public class ArgumentExceptions extends Exception implements Iterable< ArgumentException> {

	private static final long serialVersionUID = -4664872800886363165L;

	private final Collection< ArgumentException> argumentExceptions;

	public ArgumentExceptions( final Collection< ? extends ArgumentException> exceptions) {
		this.argumentExceptions = Collections.unmodifiableCollection( new LinkedList<>( exceptions));
	}

	public Collection< ArgumentException> getArgumentExceptions() {
		return this.argumentExceptions;
	}

	@ Override
	public Iterator< ArgumentException> iterator() {
		return this.argumentExceptions.iterator();
	}
}
