实现pagerank

1、定义收敛标准
	每次算出新的pr-oldpr=差值 ，所有页面的差值累加 ,除以pagecount，得到avg差值 ，如果。小于0.01
2、计算总页面数，并且算出每个页面的初始pr值=1/pagecount
3、
	A  0.25 B  D ----- A  0.35 B D--- A  0.29 B D----