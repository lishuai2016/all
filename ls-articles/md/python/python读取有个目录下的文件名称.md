
```
#读取一个文件夹下所有文件名（去掉文件的扩展名）
import os
filenames=os.listdir(os.getcwd())
for name in filenames:
     filenames[filenames.index(name)]=name
out=open('names.txt','w')
for name in filenames:
     out.write(name+'\n')
out.close()

```
