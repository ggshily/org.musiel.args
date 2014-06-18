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
import java.util.List;
import java.util.Map;

import org.musiel.args.syntax.GnuSyntax;
import org.musiel.args.syntax.Syntax;
import org.musiel.args.syntax.Syntax.SyntaxResult;

public class DefaultParser extends AbstractParser< DefaultResult> {

	public DefaultParser() {
		this( new GnuSyntax());
	}

	public DefaultParser( final Syntax syntax) {
		super( syntax);
	}

	@ Override
	public void setResourceBundleBase( final String base) {
		super.setResourceBundleBase( base);
	}

	@ Override
	public void setDescription( final String description) {
		super.setDescription( description);
	}

	public Option addOption( final String primaryName, final String... additionalNames) {
		return this.addOption( primaryName, additionalNames, false, false, ArgumentPolicy.NONE, null, null);
	}

	@ Override
	protected Option addOption( final String primaryName, final String[] additionalNames, final boolean required,
			final boolean repeatable, final ArgumentPolicy argumentPolicy, final String description, final String argumentName) {
		return super.addOption( primaryName, additionalNames, required, repeatable, argumentPolicy, description, argumentName);
	}

	@ Override
	public Option addOption( final String primaryName, final Collection< String> additionalNames, final boolean required,
			final boolean repeatable, final ArgumentPolicy argumentPolicy, final String description, final String argumentName) {
		return super.addOption( primaryName, additionalNames, required, repeatable, argumentPolicy, description, argumentName);
	}

	@ Override
	public void setOperandPattern( final String operandPattern) {
		super.setOperandPattern( operandPattern);
	}

	@ Override
	public void setOperandDescription( final String operandName, final String operandDescription) {
		super.setOperandDescription( operandName, operandDescription);
	}

	@ Override
	protected DefaultResult buildResult( final SyntaxResult syntaxResult, final Map< String, List< String>> operands,
			final Collection< ? extends ArgumentException> exceptions) {
		return new DefaultResult( syntaxResult, operands, exceptions);
	}
}
