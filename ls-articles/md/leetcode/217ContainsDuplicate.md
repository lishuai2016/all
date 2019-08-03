# 217. Contains Duplicate

[LeetCode地址](https://leetcode-cn.com/problems/contains-duplicate/submissions/)

## 题目描述
给定一个整数数组，判断是否存在重复元素。

如果任何值在数组中出现至少两次，函数返回 true。如果数组中每个元素都不相同，则返回 false。

示例 1:
输入: [1,2,3,1]
输出: true

示例 2:
输入: [1,2,3,4]
输出: false

示例 3:
输入: [1,1,1,3,3,4,3,2,4,2]
输出: true


## 思路
- 思路1：直接遍历，时间复杂度为N*N;
- 思路2：使用hashSet保存数字，时间复杂度为N，空间复杂度为N;
- 思路3：先排序再遍历一遍比较相邻的元素，时间复杂度为NlogN;
- 思路4：使用bitmap保存元素，每个数字只占用1bit，相比方法2，一个数字占用一个Integer 四个字节32bit极大节约空间；【最优，推荐，前提数组中的数都是正数】

## 答案

### 思路1
```java
class Solution {
    public boolean containsDuplicate(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] == nums[j]) {
                    return true;
                }
            }
        }
        return false;
    }
}
```

### 思路2
```java
class Solution {
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            if (!set.add(nums[i])) return true; 
        }
        return false;
    }
}
```

### 思路3
```java
class Solution {
    public boolean containsDuplicate(int[] nums) {
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] == nums[i + 1]) {
                return true;
            }
        }
        return false;
    }
}
```

### 思路4  

```java
class Solution {
    public boolean containsDuplicate(int[] nums) {
        byte[] mark = new byte[150000];
         for (int i : nums) {
             int j = i/8;
             int k = i%8;
             int check = 1<<k;
             if ((mark[j] & check) != 0) {
                 return true;
             }
             mark[j]|=check;
         }
         return false;

    }
}
```

