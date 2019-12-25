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

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The {@link EventExecutorGroup} is responsible for providing the {@link EventExecutor}'s to use
 * via its {@link #next()} method. Besides this, it is also responsible for handling their
 * life-cycle and allows shutting them down in a global fashion.
 *
 * <br/>
 * EventExecutorGroup继承自jdk java.util.concurrent包下的ScheduledExecutorService,
 * 这意味着EventExecutorGroup本身就是一个标准的jdk executor, 提供定时任务的支持.
 *
 * 1> EventExecutorGroup管理了(从对象关系上说是"聚合"了)多个EventExecutor
 * 2> next()方法返回其中的一个EventExecutor
 * 3> children()返回所有的EventExecutor，iterator()
 */
public interface EventExecutorGroup extends ScheduledExecutorService, Iterable<EventExecutor> {

    /**
     * Returns {@code true} if and only if all {@link EventExecutor}s managed by this {@link EventExecutorGroup}
     * are being {@linkplain #shutdownGracefully() shut down gracefully} or was {@linkplain #isShutdown() shut down}.
     */
    /**
     * 当且仅当这个EventExecutorGroup管理的所有EventExecutor正在被shutdownGracefully()方法优雅关闭或被已经被关闭.
     */
    boolean isShuttingDown();

    /**
     * Shortcut method for {@link #shutdownGracefully(long, long, TimeUnit)} with sensible default values.
     *
     * @return the {@link #terminationFuture()}
     * 等同于以合理的默认参数调用shutdownGracefully(quietPeriod, timeout, unit)方法
     */
    Future<?> shutdownGracefully();

    /**
     * Signals this executor that the caller wants the executor to be shut down.  Once this method is called,
     * {@link #isShuttingDown()} starts to return {@code true}, and the executor prepares to shut itself down.
     * Unlike {@link #shutdown()}, graceful shutdown ensures that no tasks are submitted for <i>'the quiet period'</i>
     * (usually a couple seconds) before it shuts itself down.  If a task is submitted during the quiet period,
     * it is guaranteed to be accepted and the quiet period will start over.
     * <br/>
     *
     *发信号给这个executor,告之调用者希望这个executor关闭.
     * 一旦这个方法被调用, isShuttingDown()方法就将开始返回true, 然后这个executor准备关闭自己.
     * 和shutdown()不同, 优雅关闭保证在关闭之前,在静默时间(the quiet period, 通常是几秒钟)内没有任务提交.
     * 如果在静默时间内有任务提交, 这个任务将被接受, 而静默时间将重头开始.
     * @param quietPeriod the quiet period as described in the documentation  上面文档中描述的静默时间
     * @param timeout     the maximum amount of time to wait until the executor is {@linkplain #shutdown()}
     *                    regardless if a task was submitted during the quiet period  等待的最大超时时间, 直到executor被关闭, 无论在静默时间内是否有任务被提交
     * @param unit        the unit of {@code quietPeriod} and {@code timeout}  静默时间和超时时间的单位
     *
     * @return the {@link #terminationFuture()}
     */
    Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit);

    /**
     * Returns the {@link Future} which is notified when all {@link EventExecutor}s managed by this
     * {@link EventExecutorGroup} have been terminated.
     *
     * 获取一个Future, 当这个EventExecutorGroup管理的所有的EventExecutors都被终止时可以得到通知
     */
    Future<?> terminationFuture();

    /**
     * @deprecated {@link #shutdownGracefully(long, long, TimeUnit)} or {@link #shutdownGracefully()} instead.
     * //禁用,用shutdownGracefully()代替
     */
    @Override
    @Deprecated
    void shutdown();

    /**
     * @deprecated {@link #shutdownGracefully(long, long, TimeUnit)} or {@link #shutdownGracefully()} instead.
     *
     *  //禁用,用shutdownGracefully()代替
     */
    @Override
    @Deprecated
    List<Runnable> shutdownNow();

    /**
     * Returns one of the {@link EventExecutor}s managed by this {@link EventExecutorGroup}.
     *
     * 返回这个EventExecutorGroup管理的一个EventExecutor
     */
    EventExecutor next();

    /**
     * 返回这个EventExecutorGroup管理的所有EventExecutor的不可变集合
     * @return
     */
    @Override
    Iterator<EventExecutor> iterator();


    /**
     * EventExecutorGroup中submit()方法返回的 Future 是 "io.netty.util.concurrent.Future"
     * ExecutorService中submit()方法返回的 Future 是 "java.util.concurrent.Future"
     */


    @Override
    Future<?> submit(Runnable task);

    @Override
    <T> Future<T> submit(Runnable task, T result);

    @Override
    <T> Future<T> submit(Callable<T> task);

    @Override
    ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit);

    @Override
    <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit);

    @Override
    ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit);

    @Override
    ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit);
}
