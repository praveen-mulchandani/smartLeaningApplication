import tensorflow as tf

EXPORT_DIR = './model'

a = tf.placeholder(tf.float32, shape=6, name="input")
x1 = tf.multiply(a[0], 0.2074489, "x1")
x2 = tf.multiply(a[1], 0.55297395, "x2")
x3 = tf.multiply(a[2], 0.34011193, "x3")
x4 = tf.multiply(a[3], 0.09467932, "x4")
x5 = tf.multiply(a[4], - 0.11543267, "x5")
x6 = tf.multiply(a[5], 0.04816485, "x6")
sum1 = tf.add(x1, x2, "intermediateSum1")
sum2 = tf.add(x4, x3, "intermediateSum2")
sum3 = tf.add(x5, x6, "intermediateSum3")
sum4 = tf.add(sum1, sum2, "intermediateSum4")
finalSum = tf.add(sum4, sum3, "output")
graph = tf.get_default_graph()
operations = graph.get_operations()
print(operations)
tf.train.write_graph(graph, EXPORT_DIR, 'quiz_graph.pb', as_text=False)
