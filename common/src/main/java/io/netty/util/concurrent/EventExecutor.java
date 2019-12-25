/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.util.concurrent;

/**
 * The {@link EventExecutor} is a special {@link EventExecutorGroup} which comes
 * with some handy methods to see if a {@link Thread} is executed in a event loop.
 * Besides this, it also extends the {@link EventExecutorGroup} to allow for a generic
 * way to access methods.
 *
 * 本质上EventExecutor是管理线程的即它是个线程池
 *
 * EventExecutor的设计比较费解, 居然是从EventExecutorGroup下继承:
 * 考虑到EventExecutorGroup的设计中, EventExecutorGroup内部是聚合/管理了多个EventExecutor.
 * 然后现在EventExecutor再继承EventExecutorGroup一把, 我有些凌乱了......
 *
 * 和EventExecutorGroup的关系:
 * 在EventExecutor中覆盖了EventExecutorGroup的next()/children(), 在我看来这是netty在努力的收拾凌乱的局面
 *
 */
public interface EventExecutor extends EventExecutorGroup {

    /**
     * Returns a reference to itself.
     *
     * EventExecutorGroup中next()方法用来返回管理的EventExecutor中的其中一个, 到了EventExecutor中,
     * 这里应该不会继续再管理其他EventExecutor了, 所以next()方法被覆盖为仅仅返回当前EventExecutor本身的引用
     */
    @Override
    EventExecutor next();

    /**
     * Return the {@link EventExecutorGroup} which is the parent of this {@link EventExecutor},
     *
     * 新增加的方法,返回当前EventExecutor所属的EventExecutorGroup
     * inEventLoop()方法
     */
    EventExecutorGroup parent();

    /**
     * Calls {@link #inEventLoop(Thread)} with {@link Thread#currentThread()} as argument
     *
     * 用于查询某个线程是否在EventExecutor所管理的线程中.
     * 等同于调用inEventLoop(Thread.currentThread())
     */
    boolean inEventLoop();

    /**
     * Return {@code true} if the given {@link Thread} is executed in the event loop,
     * {@code false} otherwise.
     *
     * 当给定的线程在event loop中返回true, 否则返回false
     */
    boolean inEventLoop(Thread thread);

    /**
     * Return a new {@link Promise}.
     */
    <V> Promise<V> newPromise();

    /**
     * Create a new {@link ProgressivePromise}.
     */
    <V> ProgressivePromise<V> newProgressivePromise();

    /**
     * Create a new {@link Future} which is marked as succeeded already. So {@link Future#isSuccess()}
     * will return {@code true}. All {@link FutureListener} added to it will be notified directly. Also
     * every call of blocking methods will just return without blocking.
     */
    <V> Future<V> newSucceededFuture(V result);

    /**
     * Create a new {@link Future} which is marked as failed already. So {@link Future#isSuccess()}
     * will return {@code false}. All {@link FutureListener} added to it will be notified directly. Also
     * every call of blocking methods will just return without blocking.
     */
    <V> Future<V> newFailedFuture(Throwable cause);
}
