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

import org.musiel.args.ArgumentException;

public class OptionException extends ArgumentException {

	private static final long serialVersionUID = -4338828846948501743L;

	private final String optionName;

	public String getOptionName() {
		return this.optionName;
	}

	public OptionException( final String optionName, final String message) {
		super( message);
		this.optionName = optionName;
	}

	public OptionException( final String optionName, final String messageBundleBase, final String messageKey,
			final Object... messageParameters) {
		super( messageBundleBase, messageKey, messageParameters);
		this.optionName = optionName;
	}
}
