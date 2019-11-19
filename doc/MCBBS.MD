# MiaoBind 喵式绑定

- 构建状态 [![Build Status](https://ci.yumc.pw/job/Minecraft/job/MiaoBind/badge/icon?style=flat-square)](https://ci.yumc.pw/job/Minecraft/job/MiaoBind/)
- 当前版本 ![Build Status](https://ci.yumc.pw/job/Minecraft/job/MiaoBind/badge/icon?style=flat-square&subject=version&status=1.2.5&color=darkturquoise)
- 下载地址 ![Build Status](https://ci.yumc.pw/job/Minecraft/job/MiaoBind/badge/icon?style=flat-square&subject=download&status=MiaoBind&color=darkgreen)
[hide]
下载后请顺手评分喔，评分加人气不会扣除自己的人气
[![Build Status](https://ci.yumc.pw/job/Minecraft/job/MiaoBind/badge/icon?style=flat-square&subject=MiaoMarket&status=Download&color=red)](http://w.yumc.pw/free.html#MiaoBind-download)<=下载按钮
当前版本兼容了1.9-1.14.4的新物品 所以可能存在部分问题 
有问题请加群反馈 点击链接加入群聊[【喵式插件交流群】](https://jq.qq.com/?_wv=1027&k=5ZpDy69)
[/hide]

### 插件简介

- 没错 就是那个众筹的 MiaoBind 插件
- 继 MiaoMenu 之后 又一个插件公开了
- 此插件 兼容 `SoulBound` 绑定标签
- 支持多 Tag 绑定
- 支持 ESS 物品发放 (当前理论上 只要是支持Lore的 都可以)
- 新增了多种安全机制 防止绑定物品丢失 (感谢 jiongjiong 提供的部分情况)
  - 盔甲架限制
  - 副手切换处理
  - 满背包情况下 禁止点击绑定物品
  - 执行某种命令后 检查背包装备栏
- 新增了异常掉线 上线后手动恢复的功能
- 新增了时限绑定的功能 (/bindtime Mr_jtb 2019-10-25 00:00:00)
- 更多功能请自行体验

### 插件使用演示

- 基础功能演示
- ![图片](https://coding-net-production-pp-ci.codehub.cn/81d17001-5fe8-4403-bfd1-782504437db4.gif)
- 安全防护演示
- ![图片](https://coding-net-production-pp-ci.codehub.cn/8369d2eb-5f20-4170-8109-39a2f88634d5.gif)
- 死亡不掉落演示(新增了异情况处理 例如 死亡后掉线 背包异常塞满)
- ![图片](https://coding-net-production-pp-ci.codehub.cn/5ba422ca-b104-41cc-828c-0e6c6f109d43.gif)
- 服务器意外崩溃 玩家未复活 通过保存机制保留数据
- ![图片](https://coding-net-production-pp-ci.codehub.cn/e07381eb-1c30-4577-b749-241de872b575.gif)

### 配置文件

```yaml
#配置文件版本 请勿修改
Version: 1.2

#绑定显示样式
Style:
  #隐藏玩家名称
  HideName: false
  #到期时间格式
  DateFormat: 'yyyy-MM-dd HH:mm:ss'

#绑定标签处理
#检查操作 检索所有标签
#绑定操作 使用第一个标签
Tag:
  Bind:
  - '§6喵式绑定'
  - '§6灵魂绑定'
  - '§6SoulBound'
  TimeBind:
  - '§6到期时间'
  BindOnPickup:
  - '§4拾取后绑定'
  BindOnUse:
  - '§4使用后绑定'
  BindOnEquip:
  - '§4装备后绑定'

#检查装备栏
CheckArmor:
  #特殊命令(一些可以直接装备的命令)
  Command:
  - '/hat'

#设置
Setting:
  #死亡不掉落绑定物品
  #注意: 前提是服务器开启死亡掉落 不然全部不会掉落
  KeepOnDeath: true
  #不允许丢弃
  PreventDrop: true
  #丢弃后删除
  DeleteOnDrop: false
  #是否允许放到容器里
  AllowStore: true
  #是否允许绑定堆叠物品(物品数量大于1)
  AllowStack: true
```

### 命令帮助

```
========= [MiaoBind] 帮助 By 喵♂呜 =========
/bind  - 绑定物品
/bindtime <玩家名称> 到期时间[xxxx-xx-xx xx:xx:xx] - 绑定时限物品
/bindonequip[boe]  - 装备时绑定物品
/bindonpickup[bop]  - 拾取时绑定物品
/bindonuse[bou]  - 使用时绑定物品
/unbind[ub]  - 解绑物品
查看更多的帮助页面 请输入 /miaobind help 1-1
```

### 权限列表

```
插件命令权限: 
 - MiaoBind.bind - 命令 bind 的权限
 - MiaoBind.bindtime - 命令 bindtime 的权限
 - MiaoBind.bindonequip - 命令 bindonequip 的权限
 - MiaoBind.bindonpickup - 命令 bindonpickup 的权限
 - MiaoBind.bindonuse - 命令 bindonuse 的权限
 - MiaoBind.unbind - 命令 unbind 的权限
插件注册权限: 
 - MiaoBind.keepondeath - 死亡保留物品权限!
 - MiaoBind.default - MiaoBind 默认权限!
 - MiaoBind.admin - MiaoBind 管理员权限!
 - MiaoBind.reload - 重新载入插件!
```

### 更新日志

- 19-11-19 feat: 新增 AllowStack 配置 限制多个物品绑定
- 19-11-18 feat: 新增 BindItemEvent 事件 方便其他插件操作
- 19-11-17 fix: 修复reload命令 未更新绑定标签的BUG

本插件所用所有代码均为原创,不存在借用/抄袭等行为
