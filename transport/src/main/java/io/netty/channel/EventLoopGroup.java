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
package io.netty.channel;

import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Special {@link EventExecutorGroup} which allows registering {@link Channel}s that get
 * processed for later selection during the event loop.
 * 是一个特殊的容许注册Channel的EventExecutorGroup.
 *
 * EventLoopGroup/EventLoop的关系和EventExecutorGroup/EventExecutor的关系非常类似,
 * 都是费解的聚合+继承, 另外EventLoopGroup/EventLoop分别继承自EventExecutorGroup/EventExecutor.
 *
 */
public interface EventLoopGroup extends EventExecutorGroup {
    /**
     * Return the next {@link EventLoop} to use
     *
     * // 返回类型从EventExecutor变成了EventLoop
     */
    @Override
    EventLoop next();



    //    register是EventLoopGroup的核心, 用来注册Channel
    //    javadoc中有个注意的提醒: 只有当ChannelPromise成功后, 从ChannelHandler提交新任务到EventLoop中才是安全的, 否则这个任务可能被拒绝. 这个好理解, 只有Channel注册成功之后, 才能开始提交任务.
    /**
     * Register a {@link Channel} with this {@link EventLoop}. The returned {@link ChannelFuture}
     * will get notified once the registration was complete.
     *
     * 将Channel注册到EventLoopGroup中的一个EventLoop.当注册完成时,返回的ChannelFuture会得到通知.
     *
     */
    ChannelFuture register(Channel channel);

    /**
     * Register a {@link Channel} with this {@link EventLoop} using a {@link ChannelFuture}. The passed
     * {@link ChannelFuture} will get notified once the registration was complete and also will get returned.
     */
    ChannelFuture register(ChannelPromise promise);

    /**
     * Register a {@link Channel} with this {@link EventLoop}. The passed {@link ChannelFuture}
     * will get notified once the registration was complete and also will get returned.
     *
     * 将Channel注册到EventLoopGroup中的一个EventLoop,当注册完成时ChannelPromise会得到通知, 而返回的ChannelFuture就是传入的ChannelPromise.
     *
     * @deprecated Use {@link #register(ChannelPromise)} instead.
     */
    @Deprecated
    ChannelFuture register(Channel channel, ChannelPromise promise);
}
