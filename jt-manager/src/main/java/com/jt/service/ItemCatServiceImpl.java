package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.anno.CacheFind;
import com.jt.unit.ObjectMapperUtil;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

import java.util.ArrayList;
import java.util.List;
//快捷键  alt+shift+p   2.alt+回车
//动态生成get/set等方法    alt+insert
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private ItemCatMapper itemCatMapper;  //如果mapper字体飘红  先暂时不管.
	/*@Autowired(required = false)
	private ShardedJedis jedis;*/
	@Autowired
	private JedisCluster jedis;

	@Override
	public ItemCat findItemCatById(Long itemCatId) {
		//利用MP机制查询数据库数据.
		return itemCatMapper.selectById(itemCatId);
	}

	/**
	 * 据接口添加实现类的方法
	 * 业务思路:
	 * 	1.用户传递的数据parentId
	 * 	2.可以查询List<ItemCat>数据库对象信息.
	 * 	3.动态的将ItemCat对象转化为EasyUITree对象
	 * 	3.但是返回值要求 返回List<EasyUITree>
	 */
	@Override
	@CacheFind(key = "ITEM_CAT_LIST")
	public List<EasyUITree> findItemCatListByParentId(Long parentId){
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent_id",parentId);
		List<ItemCat> itemCatList = itemCatMapper.selectList(queryWrapper);
		List<EasyUITree> treeList = new ArrayList<>();  //先准备一个空集合.
		//需要将数据一个一个的格式转化.
		for(ItemCat itemcat : itemCatList){
			Long id = itemcat.getId();	//获取ID
			String text = itemcat.getName();	//获取文本
			//如果是父级,则默认应该处于关闭状态 closed, 如果不是父级 则应该处于打开状态. open
			String state = itemcat.getIsParent()?"closed":"open";
			//利用构造方法 为VO对象赋值  至此已经实现了数据的转化
			EasyUITree tree = new EasyUITree(id,text,state);
			treeList.add(tree);
		}

		//用户需要返回List<EasyUITree>
		return treeList;
	}

	@Override
	public List<EasyUITree> findItemCatCache(Long parentId) {
		//1 准备key
		String key="ITEM_CAT_LIST"+parentId;
		List<EasyUITree> treeList=new ArrayList<>();
		Long startTime=System.currentTimeMillis();
		//2.判断redis中是否有数据
		if(jedis.exists(key)){
			//表示key已存在不是第一次查询.直接从redis中获取数据.返回数据
			String json = jedis.get(key);
			treeList = ObjectMapperUtil.toObject(json, treeList.getClass());
			Long endTime = System.currentTimeMillis();
			System.out.println("查询缓存的时间为:"+(endTime-startTime)+"毫秒");
		}else{
			//表示key不存在,执行数据库查询
			treeList = findItemCatListByParentId(parentId);
			Long endTime = System.currentTimeMillis();
			System.out.println("查询数据库的时间为:"+(endTime-startTime)+"毫秒");

			//2.将数据转化为json
			String  json = ObjectMapperUtil.toJSON(treeList);
			//3.将返回值结果,保存到redis中. 是否需要设定超时时间???  业务
			jedis.set(key, json);

		}
		return treeList;
	}


}
