## 适配器模式

### 概念
适配器模式将一个类的接口，转换为客户期望的另外一个接口。使得原本由于接口不兼容而不能一起工作
的那些类可以在一起工作。

### 组成
- Client（客户端）：笔记本
- Target（目标接口）：三相插头
- Adapter（适配器）：二相转三相适配器
- Adaptee（被适配类）：二相插座

### 分类

#### 组合
采用组合方式的适配器称为【对象】适配器

特点：把”被适配者“作为一个对象组合到适配器类中，以修改目标接口包装被适配者。

#### 继承（单一、重用性低）
采用继承方式的适配器称为【类】适配器

特点：通过继承被适配类和实现目标接口，实现对目标接口的匹配，单一的为某个类而实现适配。





