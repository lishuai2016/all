

# 0、MyBatis/Ibatis中#和$的区别
```
1. #将传入的数据都当成一个字符串，会对自动传入的数据加一个双引号。如：order by #user_id#，如果传入的值是111,那么解析成sql时的值为order by "111", 如果传入的值是id，则解析成的sql为order by "id".

2. $将传入的数据直接显示生成在sql中。如：order by $user_id$，如果传入的值是111,那么解析成sql时的值为order by user_id,  如果传入的值是id，则解析成的sql为order by id.

3. #方式能够很大程度防止sql注入。

4.$方式无法防止Sql注入。

5.$方式一般用于传入数据库对象，例如传入表名.

6.一般能用#的就别用$.
```

ps:在使用mybatis中还遇到<![CDATA[]]>的用法，在该符号内的语句，将不会被当成字符串来处理，而是直接当成sql语句，比如要执行一个存储过程。



# 1、查询结果的关联映射

```xml
	<resultMap type="User" id="UserResult">
		<id     property="userId"       column="user_id"      />
		<result property="deptId"       column="dept_id"      />
		<result property="loginName"    column="login_name"   />
		<result property="userName"     column="user_name"    />
		<result property="email"        column="email"        />
		<result property="phonenumber"  column="phonenumber"  />
		<result property="sex"          column="sex"          />
		<result property="avatar"       column="avatar"       />
		<result property="password"     column="password"     />
		<result property="salt"         column="salt"         />
		<result property="status"       column="status"       />
		<result property="delFlag"      column="del_flag"     />
		<result property="loginIp"      column="login_ip"     />
		<result property="loginDate"    column="login_date"   />
		<result property="createBy"     column="create_by"    />
		<result property="createTime"   column="create_time"  />
		<result property="updateBy"     column="update_by"    />
		<result property="updateTime"   column="update_time"  />
		<result property="remark"       column="remark"       />
		<association property="dept"    column="dept_id" javaType="Dept" resultMap="deptResult" />
		<collection  property="roles"   javaType="java.util.List"        resultMap="RoleResult" />
	</resultMap>
	
	<resultMap id="deptResult" type="Dept">
		<id     property="deptId"   column="dept_id"     />
		<result property="parentId" column="parent_id"   />
		<result property="deptName" column="dept_name"   />
		<result property="orderNum" column="order_num"   />
		<result property="status"   column="dept_status" />
	</resultMap>
	
	<resultMap id="RoleResult" type="Role">
		<id     property="roleId"       column="role_id"        />
		<result property="roleName"     column="role_name"      />
		<result property="roleKey"      column="role_key"       />
		<result property="roleSort"     column="role_sort"      />
		<result property="dataScope"     column="data_scope"    />
		<result property="status"       column="role_status"    />
	</resultMap>
	
	
	<select id="selectUserList" parameterType="User" resultMap="UserResult">
	...
	</select>
```

其中 user对象中除了其他字段，还包含以下的复合对象，通过<association>标签通过关联字段即可查询出相关的对象，
通过<collection> 来关联一个集合对象，resultMap属性来映射表字段和对象属性的关系
```java
public class User
{
        //....
        private Dept dept;
    
        /** 角色集合 */
        private List<Role> roles;
    
}

```


# 2、xml的别名配置

在springboot中配置别名的搜索包即可

```
mybatis:
    # 搜索指定包别名
    typeAliasesPackage: com.ruoyi.project

```

