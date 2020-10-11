package com.jt.controller;

import java.util.Arrays;
import java.util.List;

import com.jt.pojo.ItemDesc;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;

import javax.servlet.http.HttpServletRequest;

@RestController	//返回值数据都是JSON串.
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService; //业务操作

	/**
	 * 业务: 根据分页要求展现商品列表.要求将最新最热门的商品首先给用户展现.
	 * url: url:'/item/query
	 * 参数: page=1&rows=20   page当前页    rows 记录数
	 * 返回值: EasyUITable
	 * 编码习惯: mapper-service-controller-页面js   手敲  自下而上的开发
	 * 课堂代码格式:
	 * 			url-controller-service-mapper  结构代码自动生成  自上而下开发
	 *
	 */
	@RequestMapping("/query")
	public EasyUITable findItemByPage(Integer page,Integer rows) {

		//1.调用业务层,获取商品分页信息
		return itemService.findItemByPage(page,rows);
	}

	/**
	 * 1.url地址: http://localhost:8091/item/save
	 * 2.请求参数: 整个form表单
	 * 3.返回值结果: SysResult对象
	 *
	 * 复习: 页面中的参数是如何通过SpringMVC为属性赋值???
	 * 分析: 页面参数提交 一般方式3种   1.form表单提交     2.ajax页面提交  3.a标签 参数提交
	 * 		 页面参数提交一般都会遵守协议规范 key=value
	 * 分析2: SpringMVC的底层实现servlet. 包含了2大请求对象 request对象/response对象
	 * 		  servlet如何获取数据?????
	 * 规则:  参数提交的名称与mvc中接受参数的名称必须一致!!!!
	 */

	//1.利用对象的get方法,获取对象的属性的信息
	//item.getId()---->get去除------获取id的属性(大小写忽略);
	//之后将获取到的值利用对象的set方法为属性赋值.
	//request.getParameter("id")


		/*try {
			itemService.saveItem(item);
			return SysResult.success();
		}catch (Exception e){
			e.printStackTrace();
			return  SysResult.fail();
		}*/

	/**
	 * 完成2张表的入库操作
	 * @param item
	 * @return
	 */
	@RequestMapping("/save")
	public SysResult saveItem(Item item, ItemDesc itemDesc){
		itemService.saveItem(item,itemDesc);
		return SysResult.success();
	}

	/**
	 * 完成商品信息修改
	 * url:http://localhost:8091/item/update
	 * 参数: 整个商品表单
	 * 返回值: SysResult对象
	 */
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc){

		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}

	/**
	 * 业务需求: 完成商品删除操作
	 * url请求地址: /item/delete
	 * 参数: ids=  id1,id2 串
	 * 返回值结果:  SysResult对象
	 * SpringMVC知识点: 可以根据制定的类型动态的实现参数类型的转化.
	 * 					如果字符串使用","号分隔,则可以使用数组的方式接参.
	 */
	@RequestMapping("/delete")
	public SysResult deleteItems(Long[] ids){

		itemService.deleteItems(ids);
		return SysResult.success();
	}

	/**
	 * 利用restFul方式实现状态修改.
	 * 1./item/1   status=1
	 * 2./item/2   status=2
	 */
	@RequestMapping("/{status}")
	public SysResult updateStatus(@PathVariable Integer status,Long[] ids){

		itemService.updateStatus(ids,status);
		return SysResult.success();
	}

	/**
	 * 业务:根据详情ID查询商品详情信息,之后再页面中回显.
	 * url地址:http://localhost:8091/item/query/item/desc/1474391973
	 * 参数:  包含在url中,利用restFul方式动态获取
	 * 返回值:  SysResult对象
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable  Long itemId){

		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		//将服务器数据返回页面
		return SysResult.success(itemDesc);
	}





}

