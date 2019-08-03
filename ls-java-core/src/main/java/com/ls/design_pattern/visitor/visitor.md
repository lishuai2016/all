访问者模式结构图中包含以下5个角色：

- Visitor（抽象访问者）：抽象访问者为对象结构中每一个具体元素类ConcreteElement声明一个访问操作，
从这个操作的名称或参数类型可以清楚知道需要访问的具体元素的类型，具体访问者则需要实现这些操作方法，定义对这些元素的访问操作。

- ConcreteVisitor（具体访问者）：具体访问者实现了抽象访问者声明的方法，每一个操作作用于访问对象结构中一种类型的元素。

- Element（抽象元素）：一般是一个抽象类或接口，定义一个Accept方法，该方法通常以一个抽象访问者作为参数。

- ConcreteElement（具体元素）：具体元素实现了Accept方法，在Accept方法中调用访问者的访问方法以便完成一个元素的操作。

- ObjectStructure（对象结构）：对象结构是一个元素的集合，用于存放元素对象，且提供便利其内部元素的方法。


4.1 主要优点
　　（1）增加新的访问操作十分方便，不痛不痒 => 符合开闭原则

　　（2）将有关元素对象的访问行为集中到一个访问者对象中，而不是分散在一个个的元素类中，类的职责更加清晰 => 符合单一职责原则

4.2 主要缺点
　　（1）增加新的元素类很困难，需要在每一个访问者类中增加相应访问操作代码 => 违背了开闭原则

　　（2）元素对象有时候必须暴露一些自己的内部操作和状态，否则无法供访问者访问 => 破坏了元素的封装性

4.3 应用场景
　　（1）一个对象结构包含多个类型的对象，希望对这些对象实施一些依赖其具体类型的操作。=> 不同的类型可以有不同的访问操作

　　（2）对象结构中对象对应的类很少改变 很少改变 很少改变（重要的事情说三遍），但经常需要在此对象结构上定义新的操作。

参考资料


# 实例

需求背景
Background：M公司开发部想要为某企业开发一个OA系统，在该OA系统中包含一个员工信息管理子系统，该企业包括正式员工和临时工，
每周HR部门和财务部等部门需要对员工数据进行汇总，汇总数据包括员工工作时间、员工工资等等。该企业的基本制度如下：

（1）正式员工（Full time Employee）每周工作时间为40小时，不同级别、不同部门的员工每周基本工资不同；如果超过40小时，
超出部分按照50元/小时作为加班费；如果少于40小时，所缺时间按照请假处理，请假锁扣工资以80元/小时计算，直到基本工资扣除到0为止。
除了记录实际工作时间外，HR部需要记录加班时长或请假时长，作为员工平时表现的一项依据。

（2）临时员工（Part time Employee）每周工作时间不固定，基本工资按照小时计算，不同岗位的临时工小时工资不同。HR部只需要记录实际工作时间。
HR人力资源部和财务部工作人员可以根据各自的需要对员工数据进行汇总处理，HR人力资源部负责汇总每周员工工作时间，而财务部负责计算每周员工工资。

访问者1：FinanceDepartment
正式员工[梁思成] 实际工资为：[3450.0] 元
正式员工[徐志摩] 实际工资为：[2000.0] 元
正式员工[梁徽因] 实际工资为：[2240.0] 元
临时工 [方鸿渐] 实际工资为：[1600.0] 元
临时工 [唐宛如] 实际工资为：[1080.0] 元

访问者2：HRDepartment
正式员工 [梁思成] 实际工作时间为：[45] 小时
正式员工 [梁思成] 加班时间为：[5] 小时
正式员工 [徐志摩] 实际工作时间为：[40] 小时
正式员工 [梁徽因] 实际工作时间为：[38] 小时
正式员工 [梁徽因] 请假时间为：[2] 小时
临时工 [方鸿渐] 实际工作时间为：[20] 小时
临时工 [唐宛如] 实际工作时间为：[18] 小时

[参考](https://www.cnblogs.com/edisonchou/p/7247990.html)














## 11. 访问者（Visitor）

### Intent

为一个对象结构（比如组合结构）增加新能力。

### Class Diagram

- Visitor：访问者，为每一个 ConcreteElement 声明一个 visit 操作
- ConcreteVisitor：具体访问者，存储遍历过程中的累计结果
- ObjectStructure：对象结构，可以是组合结构，或者是一个集合。

<div align="center"> <img src="../pics//ec923dc7-864c-47b0-a411-1f2c48d084de.png"/> </div><br>

### Implementation

```java
public interface Element {
    void accept(Visitor visitor);
}
```

```java
class CustomerGroup {

    private List<Customer> customers = new ArrayList<>();

    void accept(Visitor visitor) {
        for (Customer customer : customers) {
            customer.accept(visitor);
        }
    }

    void addCustomer(Customer customer) {
        customers.add(customer);
    }
}
```

```java
public class Customer implements Element {

    private String name;
    private List<Order> orders = new ArrayList<>();

    Customer(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    void addOrder(Order order) {
        orders.add(order);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
        for (Order order : orders) {
            order.accept(visitor);
        }
    }
}
```

```java
public class Order implements Element {

    private String name;
    private List<Item> items = new ArrayList();

    Order(String name) {
        this.name = name;
    }

    Order(String name, String itemName) {
        this.name = name;
        this.addItem(new Item(itemName));
    }

    String getName() {
        return name;
    }

    void addItem(Item item) {
        items.add(item);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);

        for (Item item : items) {
            item.accept(visitor);
        }
    }
}
```

```java
public class Item implements Element {

    private String name;

    Item(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
```

```java
public interface Visitor {
    void visit(Customer customer);

    void visit(Order order);

    void visit(Item item);
}
```

```java
public class GeneralReport implements Visitor {

    private int customersNo;
    private int ordersNo;
    private int itemsNo;

    public void visit(Customer customer) {
        System.out.println(customer.getName());
        customersNo++;
    }

    public void visit(Order order) {
        System.out.println(order.getName());
        ordersNo++;
    }

    public void visit(Item item) {
        System.out.println(item.getName());
        itemsNo++;
    }

    public void displayResults() {
        System.out.println("Number of customers: " + customersNo);
        System.out.println("Number of orders:    " + ordersNo);
        System.out.println("Number of items:     " + itemsNo);
    }
}
```

```java
public class Client {
    public static void main(String[] args) {
        Customer customer1 = new Customer("customer1");
        customer1.addOrder(new Order("order1", "item1"));
        customer1.addOrder(new Order("order2", "item1"));
        customer1.addOrder(new Order("order3", "item1"));

        Order order = new Order("order_a");
        order.addItem(new Item("item_a1"));
        order.addItem(new Item("item_a2"));
        order.addItem(new Item("item_a3"));
        Customer customer2 = new Customer("customer2");
        customer2.addOrder(order);

        CustomerGroup customers = new CustomerGroup();
        customers.addCustomer(customer1);
        customers.addCustomer(customer2);

        GeneralReport visitor = new GeneralReport();
        customers.accept(visitor);
        visitor.displayResults();
    }
}
```

```html
customer1
order1
item1
order2
item1
order3
item1
customer2
order_a
item_a1
item_a2
item_a3
Number of customers: 2
Number of orders:    4
Number of items:     6
```

### JDK

- javax.lang.model.element.Element and javax.lang.model.element.ElementVisitor
- javax.lang.model.type.TypeMirror and javax.lang.model.type.TypeVisitor