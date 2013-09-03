package scaldingbot.net

import scala.Option.option2Iterable
import spray.http.MultipartFormData
import spray.http.BodyPart
import spray.http.FormData

trait ApiPropertyValueSet{
  val name : String
  val values : Set[String]
  override def toString =  name + "=" + values.mkString("|")
}

case class GenericApiPropertyValueSet(name : String, values : Set[String]) extends ApiPropertyValueSet

trait ApiProperty {
  val name : String
  val value : String
  implicit def toApiPropertyValueSet = GenericApiPropertyValueSet(name, Set(value))
}

class ApiPropertySet(parameters : Seq[ApiPropertyValueSet]){
  val properymap : Map[String, Set[String]] = toMap(parameters)
  
  def formatted = {
    parameters.map(p => (p.name, p.values.mkString("|")))
  }
  
  def asFormMultipart = {
    ???
  }
  
  def asFormUrlEncoded = {
    FormData(formatted.toList.toMap)
  }
  
  def +(property : ApiProperty) = {
    val newSet = mergein(properymap.get(property.name), property.value)
    val newmap = properymap + (property.name -> newSet)
    ApiPropertySet.apply(newmap)
  }
  
  def +(propery : ApiPropertyValueSet) = {
    val newSet = mergeinall(properymap.get(propery.name), propery.values)
    val newmap = properymap + (propery.name -> newSet)
    ApiPropertySet.apply(newmap)
  }
  
  def ++(properties : ApiPropertySet) = {
    val mm = properties.properymap.foldLeft(properymap)((m, i) => {
     val it =  (i._1, mergeinall(m.get(i._1), i._2))
     m + it
    })
    ApiPropertySet(mm)
  }
  
  def mergein[T](source : Option[Set[T]], add : T) = {
    source.foldLeft(Set(add))((res, a) => res ++ a)
  }
  
  def mergeinall[T](source : Option[Set[T]], add : Set[T]) = {
    source.foldLeft(add)((res, a) => res ++ a)
  }
  
  def toMap(list : Seq[ApiPropertyValueSet]) = {
    val nmap : Map[String, Set[String]] = Map.empty
    list.foldLeft(nmap)((m, apvs) => {
      val s = mergeinall(m.get(apvs.name), apvs.values)
      m + (apvs.name -> s )
    })
  }
}
 
object ApiPropertySet {
 def apply(parameters : Map[String, Set[String]]) = {
   val it = parameters.map(p => GenericApiPropertyValueSet(p._1, p._2))
   new ApiPropertySet(it.toList)
 }
 
 def apply() : ApiPropertySet = apply(Map[String, Set[String]]())
 
 def apply(props : Seq[ApiPropertyValueSet]) = new ApiPropertySet(props)
 
 def empty = apply()
}