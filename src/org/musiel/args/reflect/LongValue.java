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

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigInteger;

@ Retention( RetentionPolicy.RUNTIME)
@ Target( ElementType.METHOD)
@ Inherited
@ DecoderAnnotation( LongValue.Decoder.class)
public @ interface LongValue {

	public int radix() default 10;

	public String min() default "" + Long.MIN_VALUE;

	public String max() default "" + Long.MAX_VALUE;

	static class Decoder extends IntegerNumberDecoder< Long> {

		public Decoder( final int radix, final String min, final String max) {
			super( radix, min, max);
		}

		public Decoder( final LongValue annotation) {
			this( annotation.radix(), annotation.min(), annotation.max());
		}

		public Decoder() {
			this( 10, "" + Long.MIN_VALUE, "" + Long.MAX_VALUE);
		}

		@ Override
		protected Long cast( final BigInteger decoded) {
			return Long.valueOf( decoded.longValue());
		}
	}
}
