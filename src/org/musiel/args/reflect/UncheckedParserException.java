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
package org.musiel.args.reflect;

import java.util.Collection;
import java.util.LinkedList;

import org.musiel.args.ArgumentException;

public class UncheckedParserException extends RuntimeException {

	private static final long serialVersionUID = -5387317336480809166L;

	private final Collection< ArgumentException> possibleCauses = new LinkedList<>();

	public UncheckedParserException( final Collection< ArgumentException> possibleCauses) {
		super( possibleCauses.isEmpty()? null: possibleCauses.iterator().next());
		this.possibleCauses.addAll( possibleCauses);
	}

	public Collection< ArgumentException> getPossibleCauses() {
		return this.possibleCauses;
	}
}
