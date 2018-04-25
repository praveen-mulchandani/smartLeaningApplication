import tensorflow as tf

EXPORT_DIR = './model'

a = tf.placeholder(tf.float32, shape=6, name="input")
x1 = tf.multiply(a[0], 0.15921582, "x1")
x2 = tf.multiply(a[1], 0.51770238, "x2")
x3 = tf.multiply(a[2], 0.3084428, "x3")
x4 = tf.multiply(a[3], 0.06875411, "x4")
x5 = tf.multiply(a[4], -0.14500432, "x5")
x6 = tf.multiply(a[5], -0.00071997, "x6")
sum1 = tf.add(x1, x2, "intermediateSum1")
sum2 = tf.add(x4, x3, "intermediateSum2")
sum3 = tf.add(x5, x6, "intermediateSum3")
sum4 = tf.add(sum1, sum2, "intermediateSum4")
sum5 = tf.add(sum4, sum3, "intermediateSum5")
finalSum = tf.add(-2.0002518, sum5, "output")
graph = tf.get_default_graph()
operations = graph.get_operations()
print(operations)
tf.train.write_graph(graph, EXPORT_DIR, 'quiz_graph.pb', as_text=False)
