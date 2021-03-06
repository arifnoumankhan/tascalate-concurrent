/**
 * Original work: copyright 2013 Tomasz Nurkiewicz
 * https://github.com/nurkiewicz/async-retry
 * 
 * This class is based on the work create by Tomasz Nurkiewicz 
 * under the Apache License, Version 2.0. Please see 
 * https://github.com/nurkiewicz/async-retry/blob/master/src/main/java/com/nurkiewicz/asyncretry/backoff/RandomBackoff.java
 * 
 * Modified work: copyright 2015-2017 Valery Silaev (http://vsilaev.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tascalate.concurrent.delays;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import net.tascalate.concurrent.DelayPolicy;
import net.tascalate.concurrent.RetryContext;

abstract public class RandomDelayPolicy extends DelayPolicyWrapper {
    private final Supplier<Random> randomSource;

    protected RandomDelayPolicy(DelayPolicy target) {
        this(target, ThreadLocalRandom::current);
    }

    protected RandomDelayPolicy(DelayPolicy target, Random randomSource) {
        this(target, () -> randomSource);
    }

    private RandomDelayPolicy(DelayPolicy target, Supplier<Random> randomSource) {
        super(target);
        this.randomSource = randomSource;
    }

    @Override
    public long delayMillis(RetryContext context) {
        final long initialDelay = target.delayMillis(context);
        final long randomDelay = addRandomJitter(initialDelay);
        return Math.max(randomDelay, 0);
    }

    abstract long addRandomJitter(long initialDelay);

    protected Random random() {
        return randomSource.get();
    }
}
