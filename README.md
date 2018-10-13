# RegionCache
RegionCache是一个开源的Android区域性数据缓存工具， 不仅支持某个区域内基本类型数据，列表数据或者其他实体类数据的缓存，也支持View相关属性的缓存。对于区域内已经缓存的数据，可通过调用恢复缓存的方法将缓存数据自动恢复到指定的缓存对象中。

### Download
使用RegionCache需要添加jitpack.io的仓库：

```java
    allprojects {
         repositories {
             jcenter()
             maven { url "https://jitpack.io" }
         }
    }
```    
然后在项目的gradle文件下添加:
```java
    dependencies {
        compile 'com.github.aervon:regioncache:v1.0'
    }
```
### Using
在RegionCache中，把一个对象看成一个区域，对于这个区域对象中带有RegionCache注解的属性，将会被一键缓存，RegionCache标记如下：

```java
    // 支持对自定义对象数据的缓存，缓存对象默认需要实现Serializable（如果重新实现了数据缓存方式则不需要实现）
    @RegionCache
    public RegionData data = new RegionData();

    // 支持对各种数据集合的缓存
    @RegionCache
    public Map<String, String> map = new HashMap<>();

    @RegionCache
    public String[] stringList;

    // 支持List上指定区域的缓存
    @RegionCache(from = 0, to = 10)
    public List<String> dataList = new ArrayList<>();

    // 对于View相关属性的缓存，要求缓存的属性在对象中要有Getter和Setter方法，否则缓存无效，最多缓存三个属性，如需
    // 同时缓存多个属性建议使用实体类对制定的缓存数据进行包装再缓存
    @RegionCache(field1 = "text")
    public TextView textView;
```

执行缓存动作（同步/异步），比如执行MainActivity区域中标记属性的缓存：

```java
    // 同步缓存
    Region.with(context).cacheRegion(MainActivity.this);
    // 异步缓存
    Region.with(context).cacheRegionAsync(MainActivity.this, new CacheCallback() {
                    @Override
                    public void onCacheCompleted() {
                        Log.e("region", "onCacheCompleted");
                    }

                    @Override
                    public void onCacheError(String msg) {
                        Log.e("region", "onCacheError " + msg);
                    }
                });
```

执行读取缓存动作（同步/异步），读取后的数据会默认赋给标记的属性，比如执行MainActivity区域的读取缓存操作：

```java
    // 同步读取
    Region.with(context).cacheRegion(MainActivity.this);
    // 异步读取
    Region.with(context).readRegionAsync(MainActivity.this, new ReadCallback() {
                    @Override
                    public void onReadCompleted() {
                        Log.e("region", "onReadCompleted");
                    }

                    @Override
                    public void onReadEmpty() {
                        Log.e("region", "onReadEmpty");
                    }

                    @Override
                    public void onReadError(String msg) {
                        Log.e("region", "onReadError " + msg);
                    }
                });
 ```
 
Note： 一般请把RegionCache的读取动作放在你视图初始化动作之后，因为如果缓存属性中有View的话，如果指定的View未被初始化就会导致该View的缓存数据恢复失败。

### More
RegionCache默认的数据缓存方式为文件读写，所以当缓存对象必须为实现了Serializable序列化接口的对象，RegionCache也支持拓展数据缓存方式，比如实现以数据库为存储方式的RegionCache：

```java    
    public class DatabaseCacheProvider implements ICacheProvider {
        @Override
        public void cacheRegion(Object regionObj, String cacheTag, String cacheField, Object cacheObj) throws Exception {
            // TODO: 使用数据库实现对象缓存
        }

        @Override
        public Object readRegion(Object regionObj, String cacheTag, String cacheField) throws Exception {
            // 读取数据库缓存数据
            return null;
        }
    }
       
    Region.build(new DatabaseCacheProvider());
```

### Contact Me
邮箱联系752979730@qq.com
