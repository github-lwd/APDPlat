/**
 * 
 * APDPlat - Application Product Development Platform
 * Copyright (c) 2013, 杨尚川, yang-shangchuan@qq.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.apdplat.module.dictionary.action;

import net.sf.json.JSONArray;
import org.apdplat.module.dictionary.model.Dic;
import org.apdplat.module.dictionary.service.DicService;
import org.apdplat.platform.action.ExtJSSimpleAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

@Scope("prototype")
@Controller
@RequestMapping("/dictionary")
public class DicAction extends ExtJSSimpleAction<Dic> {
    @Resource(name = "dicService")
    private DicService dicService;
    private String dic;
    private String tree;
    private boolean justCode;
    
    /**
     * 
     * 此类用来提供下拉列表服务,主要有两种下拉类型：
     * 1、普通下拉选项
     * 2、树形下拉选项
     * @return 不需要返回值，直接给客户端写数据
     */
    @ResponseBody
    public String store(){
        Dic dictionary=dicService.getDic(dic);
        if(dictionary==null){
            LOG.info("没有找到数据词典 "+dic);
            return "";
        }
        if("true".equals(tree)){
            String json = dicService.toStoreJson(dictionary);
            return json;
        }else{
            List<Map<String,String>> data=new ArrayList<>();
            dictionary.getDicItems().forEach(item -> {
                Map<String,String> itemMap=new HashMap<>();
                if(justCode){
                    itemMap.put("value", item.getCode());
                }else{
                    itemMap.put("value", item.getId().toString());
                }
                itemMap.put("text", item.getName());
                data.add(itemMap);
            });
            String json = JSONArray.fromObject(data).toString();
            return json;
        }
    }

    public void setJustCode(boolean justCode) {
        this.justCode = justCode;
    }

    public void setTree(String tree) {
        this.tree = tree;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }
}