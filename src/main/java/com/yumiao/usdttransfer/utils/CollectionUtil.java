package com.yumiao.usdttransfer.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 
 * @author jiangmingyang
 */
public class CollectionUtil {
	public static <T> T first(Collection<T> entities,
			Function<T, Boolean> compare) {
		if (entities == null)
			return null;
		for (T t : entities)
			if (compare.apply(t))
				return t;
		return null;
	}

	public static <T> boolean firstThen(Collection<T> entities,
			Function<T, Boolean> compare, Consumer<T> then) {
		if (entities == null)
			return false;
		T t = first(entities, compare);
		if (t == null)
			return false;
		then.accept(t);
		return true;
	}

	public static <T, K> List<K> toList(Collection<T> entities,
			Function<T, K> mapper) {
		if (entities == null)
			return null;
		return entities.stream().map(mapper).filter(p -> p != null)
				.collect(Collectors.toList());
	}

	public static <T, K> List<K> toList(Collection<T> entities,
			Predicate<T> predicate, Function<T, K> mapper) {
		if (entities == null)
			return null;
		return entities.stream().filter(predicate).map(mapper)
				.filter(p -> p != null).collect(Collectors.toList());
	}

	public static <T, K> Set<K> toSet(Collection<T> entities,
			Function<T, K> mapper) {
		if (entities == null)
			return null;
		return entities.stream().map(mapper).filter(p -> p != null)
				.collect(Collectors.toSet());
	}

	public static <T, K, M extends Map<K, T>> M toMap(Collection<T> entities,
			Function<T, K> keyMapper, Supplier<M> supplier) {
		if (entities == null)
			return null;
		return entities.stream().collect(
				Collectors.toMap(keyMapper, Function.identity(), (a, b) -> b,
						supplier));
	}

	public static <T, K, V, M extends Map<K, V>> M toMap(
			Collection<T> entities, Function<T, K> keyMapper,
			Function<T, V> valueMapper, Supplier<M> supplier) {
		if (entities == null)
			return null;
		return entities.stream()
				.collect(
						Collectors.toMap(keyMapper, valueMapper, (a, b) -> b,
								supplier));
	}

	/**
	 * Convert a collection to HashMap, The key is decided by keyMapper, value
	 * is the object in collection
	 * 
	 * @param entities
	 * @param keyMapper
	 * @return
	 */

	public static <T, K> HashMap<K, T> toHashMap(Collection<T> entities,
			Function<T, K> keyMapper) {
		return toMap(entities, keyMapper, HashMap<K, T>::new);
	}

	public static <T, K> HashMap<K, T> toHashMap(T[] entities,
			Function<T, K> keyMapper) {
		return toHashMap(Arrays.asList(entities), keyMapper);
	}

	public static <T, K, V> HashMap<K, V> toHashMap(Collection<T> entities,
			Function<T, K> keyMapper, Function<T, V> valueMapper) {
		return toMap(entities, keyMapper, valueMapper, HashMap<K, V>::new);
	}

	public static <T, K, V> HashMap<K, V> toHashMap(T[] entities,
			Function<T, K> keyMapper, Function<T, V> valueMapper) {
		return toHashMap(Arrays.asList(entities), keyMapper, valueMapper);
	}

	/**
	 * Convert a collection to Hashtable. The key is decided by keyMapper, value
	 * is the object in collection
	 * 
	 * @param entities
	 * @param keyMapper
	 * @return
	 */
	public static <T, K> Hashtable<K, T> toHashtable(Collection<T> entities,
			Function<T, K> keyMapper) {
		return toMap(entities, keyMapper, Hashtable<K, T>::new);
	}

	public static <T, K> Hashtable<K, T> toHashtable(T[] entities,
			Function<T, K> keyMapper) {
		return toHashtable(Arrays.asList(entities), keyMapper);
	}

	public static <T, K, V> Hashtable<K, V> toHashtable(Collection<T> entities,
			Function<T, K> keyMapper, Function<T, V> valueMapper) {
		return toMap(entities, keyMapper, valueMapper, Hashtable<K, V>::new);
	}

	public static <T, K, V> Hashtable<K, V> toHashtable(T[] entities,
			Function<T, K> keyMapper, Function<T, V> valueMapper) {
		return toHashtable(Arrays.asList(entities), keyMapper, valueMapper);
	}

	public static <T, K, V, C extends Collection<V>> HashMap<K, C> group(
			Collection<T> entities, Function<T, K> keyMapper,
			Function<T, V> valueMapper, Supplier<C> collectionFactory) {
		if (entities == null)
			return null;
		return entities.stream().collect(
				Collectors.groupingBy(
						keyMapper,
						HashMap::new,
						Collectors.mapping(valueMapper,
								Collectors.toCollection(collectionFactory))));
	}

	public static <T, K, V> HashMap<K, HashSet<V>> group(
			Collection<T> entities, Function<T, K> keyMapper,
			Function<T, V> valueMapper) {
		return group(entities, keyMapper, valueMapper, HashSet::new);
	}

	public static <T, K> HashMap<K, List<T>> groupAsList(
			Collection<T> entities, Function<T, K> keyMapper) {
		return group(entities, keyMapper, Function.identity(), ArrayList::new);
	}

	public static <T, K, V> HashMap<K, List<V>> groupAsList(
			Collection<T> entities, Function<T, K> keyMapper,
			Function<T, V> valueMapper) {
		return group(entities, keyMapper, valueMapper, ArrayList::new);
	}

	public static <T, K, V> HashMap<K, String> groupJoining(
			Collection<T> entities, Function<T, K> keyMapper,
			Function<T, String> valueMapper, String delimiter) {
		if (entities == null)
			return null;
		return entities.stream().collect(
				Collectors.groupingBy(
						keyMapper,
						HashMap::new,
						Collectors.mapping(valueMapper,
								Collectors.joining(delimiter))));
	}

	public static <T, K, V> HashMap<K, HashSet<V>> group(T[] entities,
			Function<T, K> keyMapper, Function<T, V> valueMapper) {
		return group(Arrays.asList(entities), keyMapper, valueMapper);
	}

	/**
	 * Grouping into 2 levels
	 * 
	 * @param entities
	 * @param keyMapper1
	 * @param keyMapper2
	 * @param valueMapper
	 * @param collectionFactory
	 * @return
	 */
	public static <T, K, V, P, C extends Collection<V>> HashMap<K, HashMap<P, C>> groupTwice(
			Collection<T> entities, Function<T, K> keyMapper1,
			Function<T, P> keyMapper2, Function<T, V> valueMapper,
			Supplier<C> collectionFactory) {
		if (entities == null)
			return null;
		return entities.stream().collect(
				Collectors.groupingBy(keyMapper1, HashMap::new, Collectors
						.groupingBy(keyMapper2, HashMap::new, Collectors
								.mapping(valueMapper, Collectors
										.toCollection(collectionFactory)))));
	}

	/**
	 * Similar to grouping into 2 levels except each unit is not a Collection
	 * but a single object
	 * 
	 * @param entities
	 * @param keyMapper1
	 * @param keyMapper2
	 * @param valueMapper
	 * @return
	 */
	public static <T, K, V, P> HashMap<K, HashMap<P, V>> matrix(
			Collection<T> entities, Function<T, K> keyMapper1,
			Function<T, P> keyMapper2, Function<T, V> valueMapper) {
		if (entities == null)
			return null;
		return entities.stream().collect(
				Collectors.groupingBy(keyMapper1, HashMap::new, Collectors
						.toMap(keyMapper2, valueMapper, (a, b) -> a,
								HashMap::new)));
	}

	/**
	 * Extract attribute from objects in collection and store them in array and
	 * return.
	 * 
	 * @param entities
	 * @param keyMapper
	 * @param arr
	 * @return
	 */
	public static <T, K> K[] toArray(Collection<T> entities,
			Function<T, K> keyMapper, K[] arr) {
		if (entities == null)
			return null;
		return entities.stream().map(keyMapper).toArray(size -> arr);
	}

	/**
	 * Move an object in List up
	 * 
	 * @param list
	 * @param key
	 * @param keyMapper
	 * @return
	 */
	public static <T, K> boolean moveUp(List<T> list, K key,
			Function<T, K> keyMapper, int n) {
		if (list == null)
			return false;
		ArrayList<T> newList = new ArrayList<T>();
		boolean changed = false;
		for (int i = 0; i < list.size(); i++) {
			T item = list.get(i);

			if (i > 0 && key.equals(keyMapper.apply(item))) {
				int posi = i - n;
				if (posi < 0)
					posi = 0;
				newList.add(posi, item);
				changed = true;
			} else
				newList.add(item);
		}

		if (changed) {
			list.clear();
			list.addAll(newList);
			return true;
		}
		return false;
	}

	public static <T, K> boolean moveUp(List<T> list, K key,
			Function<T, K> keyMapper) {
		return moveUp(list, key, keyMapper, 1);
	}

	/**
	 * Move an object in List down
	 * 
	 * @param list
	 * @param key
	 * @param keyMapper
	 * @return
	 */
	public static <T, K> boolean moveDown(List<T> list, K key,
			Function<T, K> keyMapper, int n) {
		if (list == null)
			return false;
		ArrayList<T> newList = new ArrayList<T>();
		boolean changed = false;
		int start = list.size() - 1;
		for (int i = start; i >= 0; i--) {
			T item = list.get(i);

			if (i != start && key.equals(keyMapper.apply(item))) {
				int posi = n;
				if (newList.size() < posi)
					posi = newList.size();
				newList.add(posi, item);
				changed = true;
			} else
				newList.add(0, item);
		}

		if (changed) {
			list.clear();
			list.addAll(newList);
			return true;
		}
		return false;
	}

	public static <T, K> boolean moveDown(List<T> list, K key,
			Function<T, K> keyMapper) {
		return moveDown(list, key, keyMapper, 1);
	}

	/**
	 * 将列表中id为key的项向前或向后移动n个位置。n为正表示向后移动，n为负表示向前移动。
	 * 
	 * @param list
	 * @param key
	 * @param keyMapper
	 * @param n
	 * @return
	 */
	public static <T, K> boolean move(List<T> list, K key,
			Function<T, K> keyMapper, int n) {
		if (n == 0)
			return false;

		if (n > 0)
			return moveDown(list, key, keyMapper, n);

		return moveUp(list, key, keyMapper, -n);
	}

	/**
	 * 如果before为true则把key移动到ref前面，否则把key移动到ref后面
	 * 
	 * @param list
	 * @param key
	 * @param ref
	 * @param keyMapper
	 * @param before
	 * @return 被移动的元素移动后在列表中的位置，-1表示没有被移动
	 */
	public static <T, K> List<T> move(List<T> list, K key, K ref,
			Function<T, K> keyMapper, boolean before,
			Function<T, Integer> orderGet, OrderSetter<T> orderSet, int orderGap) {
		if (key == ref)
			return null;

		// { 找到要移动的项和参考项所在位置
		int keyidx = -1;
		int refidx = -1;
		int idx = 0;

		for (T t : list) {
			K id = keyMapper.apply(t);
			if (id.equals(key))
				keyidx = idx;
			if (id.equals(ref))
				refidx = idx;

			if (keyidx != -1 && refidx != -1)
				break;

			idx++;
		}

		if (keyidx == -1 || refidx == -1)
			return null;
		// }

		// { 拿走要移动的项
		T t = list.remove(keyidx);
		if (refidx >= keyidx)
			refidx--;

		if (!before)
			refidx++;
		// }

		// 放回要移动的项
		list.add(refidx, t);

		// { 设置排序位置
		ArrayList<T> moved = new ArrayList<T>();

		int startOrder = 0;
		if (refidx > 0)
			startOrder = orderGet.apply(list.get(refidx - 1));

		for (int i = refidx + 1; i < list.size(); i++) {
			int order = orderGet.apply(list.get(i));
			if (order - startOrder >= i - refidx + 1) {
				int gap = (order - startOrder) / (i - refidx + 1);
				for (int j = refidx; j < i; j++) {
					T move = list.get(j);

					orderSet.apply(move, startOrder + (j - refidx + 1) * gap);
					moved.add(move);
				}

				return moved;
			}
		}

		for (int i = refidx; i < list.size(); i++) {
			T move = list.get(i);

			orderSet.apply(move, startOrder + (i - refidx + 1) * orderGap);
			moved.add(move);
		}

		return moved;
		// }
	}

	/**
	 * 建立树形结构
	 * 
	 * @param list
	 * @param keyMapper
	 * @param parentKeyMapper
	 * @param childMapper
	 * @return
	 */
	public static <T, K> ArrayList<T> buildTree(Collection<T> list,
			Function<T, K> keyMapper, Function<T, K> parentKeyMapper,
			Function<T, Collection<T>> childMapper) {
		HashMap<K, T> map = toHashMap(list, keyMapper);

		ArrayList<T> root = new ArrayList<T>();
		for (T t : list) {
			K key = keyMapper.apply(t);
			K parentKey = parentKeyMapper.apply(t);
			if (parentKey == null || key.equals(parentKey))
				root.add(t);
			else
				childMapper.apply(map.get(parentKey)).add(t);
		}

		return root;
	}

	public static <T, K> ArrayList<T> copyTrees(Collection<T> trees,
			Function<T, K> keyMapper, Function<T, Collection<T>> childMapper,
			Function<T, T> cloneMapper, Collection<K> toCopy) {
		ArrayList<T> copy = new ArrayList<T>();

		if (toCopy == null)
			return copy;

		for (T node : trees) {
			T copyNode = copyTree(node, keyMapper, childMapper, cloneMapper,
					toCopy);
			if (copyNode != null)
				copy.add(copyNode);
		}

		return copy;
	}

	public static <T, K> T copyTree(T node, Function<T, K> keyMapper,
			Function<T, Collection<T>> childMapper, Function<T, T> cloneMapper,
			Collection<K> toCopy) {
		if (toCopy == null)
			return null;

		T copy = null;

		Collection<T> children = childMapper.apply(node);
		if (children != null && children.size() > 0) {
			for (T child : children) {
				T childCopy = copyTree(child, keyMapper, childMapper,
						cloneMapper, toCopy);
				if (childCopy != null) {
					if (copy == null)
						copy = cloneMapper.apply(node);
					childMapper.apply(copy).add(childCopy);
				}
			}
		}

		if (copy == null && toCopy.contains(keyMapper.apply(node)))
			copy = cloneMapper.apply(node);

		return copy;
	}

	public static int[] toArray(Collection<Integer> col) {
		if (col == null)
			return null;
		int[] arr = new int[col.size()];
		int i = 0;
		for (Integer v : col)
			arr[i++] = v.intValue();
		return arr;
	}

	public static <T> Collection<T> sort(Collection<T> list,
			Function<T, Number> order, boolean asc) {
		if (list == null || order == null)
			return list;

		ArrayList<T> ar = new ArrayList<T>();
		ar.addAll(list);
		ar.sort((p1, p2) -> {
			Number order1 = order.apply(p1);
			Number order2 = order.apply(p2);
			if (order1.doubleValue() > order2.doubleValue())
				return asc ? 1 : -1;
			else if (order1.doubleValue() < order2.doubleValue())
				return asc ? -1 : 1;
			return 0;
		});
		list.clear();
		list.addAll(ar);
		return list;
	}

	public static <T> Collection<T> sort(Collection<T> list,
			Function<T, Number> order) {
		return sort(list, order, true);
	}

	/**
	 * 用于为Collection中的父对象加载子对象。子对象加载应以父对象的key的集合为条件，父对象中应该有一个集合用于存放子对象。
	 * 
	 * 例如父对象Parent有属性id和集合childs，子对象Child有属性parentId。假设
	 * loader.load()可以根据parentId加载子对象集合，对于类型为List<Parent>的parents：
	 * 
	 * <pre>
	 * loadChild(parents, Parent::getId, Parent::getChilds, ids -&gt; loader.load(ids),
	 * 		Child::getParentId);
	 * </pre>
	 */
	public static <T, K, C> void loadChildren(Collection<T> list,
			Function<T, K> keyMapper,
			Function<T, Collection<C>> childCollectionMapper,
			Function<Collection<K>, Collection<C>> loader,
			Function<C, K> childKeyMapper) {
		if (list == null || list.size() == 0)
			return;

		HashMap<K, T> map = toHashMap(list, keyMapper);
		loader.apply(map.keySet()).forEach(
				p -> childCollectionMapper.apply(
						map.get(childKeyMapper.apply(p))).add(p));
	}

	/**
	 * list为装有类型为T的对象的集合，称为集合一。用T对象中通过keyGetter获得的key值组成集合，调用
	 * loader，可以获得类型为S的对象集合，称为集合二。T和S的关系是N对1。把集合一中通过
	 * keyGetter获得的关键值与集合二中通过loadedKeyGetter获得关键值相同的T和S对象匹配，调用match。
	 */
	public static <T, K, S, X extends Collection<T>> X loadNToOne(X list,
			Function<T, K> keyGetter,
			Function<Collection<K>, Collection<S>> loader,
			Function<S, K> loadedKeyGetter, BiConsumer<T, S> match) {
		if (list == null || list.size() == 0)
			return list;

		Set<K> keys = toSet(list, keyGetter);
		if (keys == null || keys.size() == 0)
			return list;

		Collection<S> subs = loader.apply(keys);
		if (subs == null || subs.size() == 0)
			return list;

		HashMap<K, S> map = toHashMap(subs, loadedKeyGetter);
		for (T item : list) {
			K key = keyGetter.apply(item);
			if (key == null)
				continue;

			S sub = map.get(key);
			if (sub == null)
				continue;
			match.accept(item, sub);
		}

		return list;
	}

	/**
	 * list为装有类型为T的对象的集合，称为集合一。用T对象中通过keyGetter获得的key值组成集合，调用
	 * loader，可以获得类型为S的对象集合，称为集合二。T和S的关系是1对N。把集合一中通过
	 * keyGetter获得的关键值与集合二中通过loadedKeyGetter获得关键值相同的T和S对象匹配，调用match。
	 */
	public static <T, K, S, X extends Collection<T>> X loadOneToN(X list,
			Function<T, K> keyGetter,
			Function<Collection<K>, Collection<S>> loader,
			Function<S, K> loadedKeyGetter, BiConsumer<T, S> match) {
		if (list == null || list.size() == 0)
			return list;

		Set<K> keys = toSet(list, keyGetter);
		if (keys == null || keys.size() == 0)
			return list;

		Collection<S> subs = loader.apply(keys);
		if (subs == null || subs.size() == 0)
			return list;

		HashMap<K, T> map = toHashMap(list, keyGetter);
		for (S sub : subs) {
			T item = map.get(loadedKeyGetter.apply(sub));
			if (item == null)
				continue;
			match.accept(item, sub);
		}

		return list;
	}

	public static <T> List<T> asList(T obj) {
		if (obj == null)
			return null;
		List<T> list = new ArrayList<T>();
		list.add(obj);
		return list;
	}

	/**
	 * 把obj包装为集合，调用function
	 */
	public static <T> T callAsCollection(T obj, Consumer<Collection<T>> function) {
		if (obj == null)
			return null;
		List<T> list = new ArrayList<T>();
		list.add(obj);
		function.accept(list);
		return obj;
	}

	public static <T, K> T findOrFirst(Collection<K> list, Function<K, T> mapper,
			T toFind) {
		if (list == null || list.size() == 0)
			return null;
		if (toFind != null)
			for (K k : list)
				if (mapper.apply(k).equals(toFind))
					return toFind;
		return mapper.apply(list.iterator().next());
	}
}
