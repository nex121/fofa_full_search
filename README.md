# fofa_full_search

融合了fofa及hunter部分语法

有历史记录功能

fofa全量搜索，资产过多自动遍历省市

支持简陋漏洞验证

详细了解可以看源码

## 工具部分截图

### 新增漏洞管理

漏洞管理升级，可以选择匹配相应头，相应正文，或者选择dig或ceye来验证无回显，反序列化等复杂漏洞。对于验证该类复杂漏洞使用ceye及dig时，建议将线程跳到5以下。

![image](https://github.com/nex121/fofa_full_search/assets/29255605/5ef55127-3918-4adc-935b-1e160b0f4e6a)

右键新增节点

![image](https://github.com/nex121/fofa_full_search/assets/29255605/ef7c52e5-2d45-41cd-9d6b-d6188a6090fe)

直接输入漏洞数据包（burp的数据包即可，host可不去除），及返回特征。点击保存，使他们与节点及相应特征绑定。

![image](https://github.com/nex121/fofa_full_search/assets/29255605/95011afa-0871-45a3-8074-d871eebc0a56)


### 配置文件

![image](https://user-images.githubusercontent.com/29255605/236624425-2c7c0264-1382-4264-8668-320a516adc20.png)

### 历史记录

![image](https://user-images.githubusercontent.com/29255605/236624572-dec97133-1801-41ee-ad3d-764a2a96c37b.png)

### 查询结果

![image](https://user-images.githubusercontent.com/29255605/236624687-52dd2db9-5beb-4fe7-a41e-2eaa897bb586.png)

### 漏洞列表

漏洞列表略有改变，当目标地址的textarea存在url列表时，点击验证按钮优先验证目标地址中的url列表，如果没有会验证fofa查询结果的url。

![image](https://github.com/nex121/fofa_full_search/assets/29255605/5f5baec1-4930-4bb1-998d-e45c9df8d2ac)

# 新写项目肯定有bug，欢迎提交。
