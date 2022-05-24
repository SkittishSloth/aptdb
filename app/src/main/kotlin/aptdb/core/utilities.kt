package aptdb.core


fun <T> Iterable<T>.windowedBy(terminator: (T) -> Boolean): List<List<T>> {
  val res: MutableList<List<T>> = mutableListOf()
  var current: MutableList<T> = mutableListOf()
  for (t in this) {
    if (terminator(t)) {
      res.add(current)
      current = mutableListOf()
    } else {
      current.add(t)
    }
  }
  
  return res
}