package com.nostra13.universalimageloader.cache.memory.gifmemory.impl;

import static com.nostra13.universalimageloader.cache.memory.bitmapmemory.impl.LruMemoryCache.size;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifDrawable;

import com.nostra13.universalimageloader.cache.memory.gifmemory.GifMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;

/**
 * A cache that holds strong references to a limited number of Bitmaps. Each time a Bitmap is accessed, it is moved to
 * the head of a queue. When a Bitmap is added to a full cache, the Bitmap at the end of that queue is evicted and may
 * become eligible for garbage collection.<br />
 * <br />
 * <b>NOTE:</b> This cache uses only strong references for stored Bitmaps.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.1
 */
public class LruGifMemoryCache implements GifMemoryCache {

	private final LinkedHashMap<String, GifDrawable> map;

	private final int maxSize;
//	/** Size of this cache in bytes */
//	private long size;

	/** @param maxSize Maximum sum of the sizes of the Bitmaps in this cache */
	public LruGifMemoryCache(int maxSize) {
		if (maxSize <= 0) {
			throw new IllegalArgumentException("maxSize <= 0");
		}
		this.maxSize = maxSize;
		this.map = new LinkedHashMap<String, GifDrawable>(0, 0.75f, true);
	}

	/**
	 * Returns the Bitmap for {@code key} if it exists in the cache. If a Bitmap was returned, it is moved to the head
	 * of the queue. This returns null if a Bitmap is not cached.
	 */
	@Override
	public final GifDrawable get(String key) {
		if (key == null) {
			throw new NullPointerException("key == null");
		}

		synchronized (this) {
			return map.get(key);
		}
	}

	/** Caches {@code Bitmap} for {@code key}. The Bitmap is moved to the head of the queue. */
	@Override
	public final boolean put(String key, GifDrawable value) {
		if (key == null || value == null) {
			throw new NullPointerException("key == null || value == null");
		}

		synchronized (this) {
			size += sizeOf(key, value);
			GifDrawable previous = map.put(key, value);
			if (previous != null) {
				size -= sizeOf(key, previous);
			}
		}

		trimToSize(maxSize);
		return true;
	}

	/**
	 * Remove the eldest entries until the total of remaining entries is at or below the requested size.
	 *
	 * @param maxSize the maximum size of the cache before returning. May be -1 to evict even 0-sized elements.
	 */
	private void trimToSize(int maxSize) {
		while (true) {
			String key;
			GifDrawable value;
			synchronized (this) {
				if (size < 0 || (map.isEmpty() && size != 0)) {
//					throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
				    //TODO:稍作修改，两个缓存采用同一size，这个地方必定会报错,map==null的时候size必定不等于0
				    L.e(getClass().getName(), getClass().getName() + ".sizeOf() is reporting inconsistent results!");
                    break;
				}

				if (size <= maxSize || map.isEmpty()) {
					break;
				}

				Map.Entry<String, GifDrawable> toEvict = map.entrySet().iterator().next();
				if (toEvict == null) {
					break;
				}
				key = toEvict.getKey();
				value = toEvict.getValue();
				map.remove(key);
				ImageLoader.getInstance().clearRecoveryMemoryCacheKey(key);
				size -= sizeOf(key, value);
			}
		}
	}

	/** Removes the entry for {@code key} if it exists. */
	@Override
	public final GifDrawable remove(String key) {
		if (key == null) {
			throw new NullPointerException("key == null");
		}

		synchronized (this) {
		    GifDrawable previous = map.remove(key);
			if (previous != null) {
				size -= sizeOf(key, previous);
			}
			return previous;
		}
	}

	@Override
	public Collection<String> keys() {
		synchronized (this) {
			return new HashSet<String>(map.keySet());
		}
	}

	@Override
	public void clear() {
		trimToSize(-1); // -1 will evict 0-sized elements
	}

	/**
	 * Returns the size {@code Bitmap} in bytes.
	 * <p/>
	 * An entry's size must not change while it is in the cache.
	 */
	private long sizeOf(String key, GifDrawable value) {
//		return value.getNumberOfFrames() * value.getIntrinsicWidth() * value.getIntrinsicHeight() * 4;//一个像数占4个字节
		return value.getAllocationByteCount();
	}

	@Override
	public synchronized final String toString() {
		return String.format("LruCache[maxSize=%d]", maxSize);
	}
}