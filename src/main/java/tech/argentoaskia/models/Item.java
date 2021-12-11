package tech.argentoaskia.models;

/**
 *
 * 顶层元素父接口接口，所有元素都要继承自这个接口.
 * <P>
 *     使用这个接口可以最大限度地兼容节点和管理节点，考虑到后期可能会有自定义<code>ini</code>语法地引入，
 * 使用这个接口可以进行多个语法语法元素地扩展.
 * <P>
 *     后期地元素扩展只需要继承自该接口，就能实现自定义扩展元素。
 * <p>
 *     预设值的Item继承有四个:
 *     <ul>
 *         <li>{@link tech.argentoaskia.models.interfaces.DocumentItem}</li>
 *         <li>{@link tech.argentoaskia.models.interfaces.SessionItem}</li>
 *         <li>{@link tech.argentoaskia.models.interfaces.KeyItem}</li>
 *         <li>{@link tech.argentoaskia.models.interfaces.ValueItem}</li>
 *     </ul>
 *     对于一个名为<code>sessionInit.ini</code>的文件的内容：
 *     <pre>
 *         [session1]
 *         key1 = value1
 *         key2 = value2
 *         [session2]
 *         key3 = value3
 *         key4 = value4
 *     </pre>
 *     将会被解析成
 *     <ul>
 *         <li>一个<code>documentItem</code>，代表code>sessionInit.ini</code>本身</li>
 *         <li>两个<code>sessionItem</code>，代表session1和session2</li>
 *         <li>四个<code>keyItem</code>，代表key1、key2、key3和key4</li>
 *         <li>四个<code>valueItem</code>，代表value1、value2、value3和value4</li>
 *     </ul>
 *
 * @param <T> 元素关键值的类型
 * @author Askia
 * @version 1.0
 */
public interface Item<T> {
    /**
     * 该方法将需要返回元素的关键值.
     * <p>
     *     所谓关键值就是指一个元素节点或者接口的关键数据，如：对于一个<code>key</code>来说，
     *     关键值就是它的key值，也就是等号左边的内容，对于一个<code>session</code>来说，
     *     关键值就是它的session名，也就是中括号里面的内容。
     * @return 元素关键值
     */
    T get();

    /**
     * 以字符串的形式获取该元素的元素类型名称.
     * <p>
     *     <strong>该方法为保留方法，子类可以有选择地实现该方法，但是请删除重写的<code>@Override</code></strong>
     * @deprecated 该方法目前为保留状态，具体要不要留，要根据后期的版本开发来决定，本质上判别一个类型，
     *             可以使用<code>instance of</code>，此方法的存在的必要性存疑。
     * @return 元素类型字符串名
     */
    @Deprecated(since = "1.0")
    default String getItemTypeName(){
        return "Item";
    }

    Object output();
}
