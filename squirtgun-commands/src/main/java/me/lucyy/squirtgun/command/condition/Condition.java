/*
 * Copyright © 2021 Lucy Poulton
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package me.lucyy.squirtgun.command.condition;

import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;
import me.lucyy.squirtgun.platform.audience.SquirtgunPlayer;

/**
 * TODO javadoc
 *
 * @param <T> the input holder type
 * @param <U> the outputted holder type
 */
@FunctionalInterface
public interface Condition<T extends PermissionHolder, U extends PermissionHolder> {

    Condition<? extends PermissionHolder, SquirtgunPlayer> isPlayer = ((target, context) -> {
        if (target instanceof SquirtgunPlayer) {
            return new Result<>(true, (SquirtgunPlayer) target, null);
        }
        return new Result<>(false, null, "This command can only be run by a player.");
    });

    static <V extends PermissionHolder> Condition<V, V> empty() {
        return (target, context) -> new Result<>(true, target, null);
    }

    static <V extends PermissionHolder> Condition<V, V> hasPermission(String permission) {
        return ((target, context) -> {
            if (target.hasPermission(permission)) {
                return new Result<>(true, target, null);
            }
            return new Result<>(false, null, "No permission!");
        });
    }

    class Result<U> {
        private final boolean successful;
        private final U result;
        private final String error;

        public Result(boolean successful, U result, String error) {
            this.successful = successful;
            this.result = result;
            this.error = error;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public U getResult() {
            return result;
        }

        public String getError() {
            return error;
        }
    }

    Result<U> test(T target, CommandContext context);

    default <V extends PermissionHolder> Condition<T, V> and(Condition<? super U, V> other) {
        return ((target, context) -> {
            Result<U> first = this.test(target, context);
            if (!first.isSuccessful()) return new Result<V>(false, null, first.getError());

            return other.test(first.getResult(), context);
        });
    }

    default Condition<T, U> or(Condition<? super T, U> other) {
        return ((target, context) -> {
            Result<U> first = this.test(target, context);
            if (first.isSuccessful()) return first;

            return other.test(target, context);
        });
    }
}