// Generated by <a href="http://scalaxb.org/">scalaxb</a>.
package com.vektorraum.aviatorsbot.generated.metar

import scala.concurrent.Future


/**
usage:
val obj = scalaxb.fromXML[com.vektorraum.aviatorsbot.generated.metar.Foo](node)
val document = scalaxb.toXML[com.vektorraum.aviatorsbot.generated.metar.Foo](obj, "foo", com.vektorraum.aviatorsbot.generated.metar.defaultScope)
**/
object `package` extends XMLProtocol { }

trait XMLProtocol extends scalaxb.XMLStandardTypes {
  implicit lazy val executionContext = scala.concurrent.ExecutionContext.Implicits.global

  val defaultScope = scalaxb.toScope(Some("xs") -> "http://www.w3.org/2001/XMLSchema",
    Some("xsi") -> "http://www.w3.org/2001/XMLSchema-instance")
  implicit lazy val Comvektorraumaviatorsbotgenerated_ResponseFormat: scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.Response] = new DefaultComvektorraumaviatorsbotgenerated_ResponseFormat {}
  implicit lazy val Comvektorraumaviatorsbotgenerated_Data_sourceFormat: scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.Data_source] = new DefaultComvektorraumaviatorsbotgenerated_Data_sourceFormat {}
  implicit lazy val Comvektorraumaviatorsbotgenerated_RequestFormat: scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.Request] = new DefaultComvektorraumaviatorsbotgenerated_RequestFormat {}
  implicit lazy val Comvektorraumaviatorsbotgenerated_ErrorsFormat: scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.Errors] = new DefaultComvektorraumaviatorsbotgenerated_ErrorsFormat {}
  implicit lazy val Comvektorraumaviatorsbotgenerated_ErrorsSequence1Format: scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.ErrorsSequence1] = new DefaultComvektorraumaviatorsbotgenerated_ErrorsSequence1Format {}
  implicit lazy val Comvektorraumaviatorsbotgenerated_WarningsFormat: scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.Warnings] = new DefaultComvektorraumaviatorsbotgenerated_WarningsFormat {}
  implicit lazy val Comvektorraumaviatorsbotgenerated_WarningsSequence1Format: scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.WarningsSequence1] = new DefaultComvektorraumaviatorsbotgenerated_WarningsSequence1Format {}
  implicit lazy val Comvektorraumaviatorsbotgenerated_DataFormat: scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.Data] = new DefaultComvektorraumaviatorsbotgenerated_DataFormat {}
  implicit lazy val Comvektorraumaviatorsbotgenerated_DataSequence1Format: scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.DataSequence1] = new DefaultComvektorraumaviatorsbotgenerated_DataSequence1Format {}
  implicit lazy val Comvektorraumaviatorsbotgenerated_METARFormat: scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.METAR] = new DefaultComvektorraumaviatorsbotgenerated_METARFormat {}
  implicit lazy val Comvektorraumaviatorsbotgenerated_Quality_control_flagsFormat: scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.Quality_control_flags] = new DefaultComvektorraumaviatorsbotgenerated_Quality_control_flagsFormat {}
  implicit lazy val Comvektorraumaviatorsbotgenerated_Sky_conditionFormat: scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.Sky_condition] = new DefaultComvektorraumaviatorsbotgenerated_Sky_conditionFormat {}


  implicit val fromAnySchemaType: scala.xml.Elem => Option[scalaxb.DataRecord[Any]] = {elem =>
    import scalaxb.{Helper, DataRecord, fromXML}

    val ns = Helper.nullOrEmpty(elem.scope.getURI(elem.prefix))
    val key = Some(elem.label)
    val (xsns, xstype) = Helper.instanceType(elem)

    (key, ns) match {
      case (Some("elevation_m"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("vert_vis_ft"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Int](elem)))
      case (Some("pcp24hr_in"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("pcp3hr_in"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("minT24hr_c"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("minT_c"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("three_hr_pressure_tendency_mb"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("sky_condition"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[com.vektorraum.aviatorsbot.generated.metar.Sky_condition](elem)))
      case (Some("present_weather_sensor_off"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))
      case (Some("lightning_sensor_off"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))
      case (Some("maintenance_indicator_on"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))
      case (Some("auto"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))
      case (Some("quality_control_flags"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[com.vektorraum.aviatorsbot.generated.metar.Quality_control_flags](elem)))
      case (Some("altim_in_hg"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("wind_gust_kt"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Int](elem)))
      case (Some("wind_dir_degrees"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Int](elem)))
      case (Some("temp_c"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("latitude"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("station_id"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))
      case (Some("METAR"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[com.vektorraum.aviatorsbot.generated.metar.METAR](elem)))
      case (Some("error"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))
      case (Some("data"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[com.vektorraum.aviatorsbot.generated.metar.Data](elem)))
      case (Some("errors"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[com.vektorraum.aviatorsbot.generated.metar.Errors](elem)))
      case (Some("data_source"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[com.vektorraum.aviatorsbot.generated.metar.Data_source](elem)))
      case (Some("response"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[com.vektorraum.aviatorsbot.generated.metar.Response](elem)))
      case (Some("request_index"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Int](elem)))
      case (Some("request"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[com.vektorraum.aviatorsbot.generated.metar.Request](elem)))
      case (Some("warnings"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[com.vektorraum.aviatorsbot.generated.metar.Warnings](elem)))
      case (Some("warning"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))
      case (Some("time_taken_ms"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Int](elem)))
      case (Some("raw_text"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))
      case (Some("observation_time"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))
      case (Some("longitude"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("dewpoint_c"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("wind_speed_kt"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Int](elem)))
      case (Some("visibility_statute_mi"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("sea_level_pressure_mb"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("corrected"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))
      case (Some("auto_station"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))
      case (Some("no_signal"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))
      case (Some("freezing_rain_sensor_off"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))
      case (Some("wx_string"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))
      case (Some("flight_category"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))
      case (Some("maxT_c"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("maxT24hr_c"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("precip_in"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("pcp6hr_in"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("snow_in"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[Float](elem)))
      case (Some("metar_type"), None) => Some(DataRecord(ns, key, xsns, xstype, fromXML[String](elem)))

      case _ => None
    }
  }

  trait DefaultComvektorraumaviatorsbotgenerated_ResponseFormat extends scalaxb.ElemNameParser[com.vektorraum.aviatorsbot.generated.metar.Response] {
    val targetNamespace: Option[String] = None
    
    def parser(node: scala.xml.Node, stack: List[scalaxb.ElemName]): Parser[com.vektorraum.aviatorsbot.generated.metar.Response] =
      phrase((scalaxb.ElemName(None, "request_index")) ~ 
      (scalaxb.ElemName(None, "data_source")) ~ 
      (scalaxb.ElemName(None, "request")) ~ 
      (scalaxb.ElemName(None, "errors")) ~ 
      (scalaxb.ElemName(None, "warnings")) ~ 
      (scalaxb.ElemName(None, "time_taken_ms")) ~ 
      (scalaxb.ElemName(None, "data")) ^^
      { case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 =>
      com.vektorraum.aviatorsbot.generated.metar.Response(scalaxb.fromXML[Int](p1, scalaxb.ElemName(node) :: stack),
        scalaxb.fromXML[com.vektorraum.aviatorsbot.generated.metar.Data_source](p2, scalaxb.ElemName(node) :: stack),
        scalaxb.fromXML[com.vektorraum.aviatorsbot.generated.metar.Request](p3, scalaxb.ElemName(node) :: stack),
        scalaxb.fromXML[com.vektorraum.aviatorsbot.generated.metar.Errors](p4, scalaxb.ElemName(node) :: stack),
        scalaxb.fromXML[com.vektorraum.aviatorsbot.generated.metar.Warnings](p5, scalaxb.ElemName(node) :: stack),
        scalaxb.fromXML[Int](p6, scalaxb.ElemName(node) :: stack),
        scalaxb.fromXML[com.vektorraum.aviatorsbot.generated.metar.Data](p7, scalaxb.ElemName(node) :: stack),
        scala.collection.immutable.ListMap(List(
        (node \ "@version").headOption map { x => scalaxb.DataRecord(x, node, scalaxb.fromXML[String](x, scalaxb.ElemName(node) :: stack)) } orElse Some(scalaxb.DataRecord(None, None, scalaxb.fromXML[String](scala.xml.Text("1.2"), scalaxb.ElemName(node) :: stack))) map { "@version" -> _ }
        ).flatten[(String, scalaxb.DataRecord[Any])]: _*)) })
    
    override def writesAttribute(__obj: com.vektorraum.aviatorsbot.generated.metar.Response, __scope: scala.xml.NamespaceBinding): scala.xml.MetaData = {
      var attr: scala.xml.MetaData  = scala.xml.Null
      __obj.attributes.toList map {
        case ("@version", _) => if (__obj.version.toString != "1.2") attr = scala.xml.Attribute(null, "version", __obj.version.toString, attr)
        case (key, x) => attr = scala.xml.Attribute((x.namespace map { __scope.getPrefix(_) }).orNull, x.key.orNull, x.value.toString, attr)
      }
      attr
    }

    def writesChildNodes(__obj: com.vektorraum.aviatorsbot.generated.metar.Response, __scope: scala.xml.NamespaceBinding): Seq[scala.xml.Node] =
      Seq.concat(scalaxb.toXML[Int](__obj.request_index, None, Some("request_index"), __scope, false),
        scalaxb.toXML[com.vektorraum.aviatorsbot.generated.metar.Data_source](__obj.data_source, None, Some("data_source"), __scope, false),
        scalaxb.toXML[com.vektorraum.aviatorsbot.generated.metar.Request](__obj.request, None, Some("request"), __scope, false),
        scalaxb.toXML[com.vektorraum.aviatorsbot.generated.metar.Errors](__obj.errors, None, Some("errors"), __scope, false),
        scalaxb.toXML[com.vektorraum.aviatorsbot.generated.metar.Warnings](__obj.warnings, None, Some("warnings"), __scope, false),
        scalaxb.toXML[Int](__obj.time_taken_ms, None, Some("time_taken_ms"), __scope, false),
        scalaxb.toXML[com.vektorraum.aviatorsbot.generated.metar.Data](__obj.data, None, Some("data"), __scope, false))

  }

  trait DefaultComvektorraumaviatorsbotgenerated_Data_sourceFormat extends scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.Data_source] with scalaxb.CanWriteChildNodes[com.vektorraum.aviatorsbot.generated.metar.Data_source] {
    val targetNamespace: Option[String] = None
    import scalaxb.ElemName._
    
    def reads(seq: scala.xml.NodeSeq, stack: List[scalaxb.ElemName]): Either[String, com.vektorraum.aviatorsbot.generated.metar.Data_source] = seq match {
      case node: scala.xml.Node => Right(com.vektorraum.aviatorsbot.generated.metar.Data_source(scala.collection.immutable.ListMap(List(
        (node \ "@name").headOption map { x => scalaxb.DataRecord(x, node, scalaxb.fromXML[String](x, scalaxb.ElemName(node) :: stack)) } map { "@name" -> _ }
        ).flatten[(String, scalaxb.DataRecord[Any])]: _*)))
      case _ => Left("reads failed: seq must be scala.xml.Node")
    }
    
    override def writesAttribute(__obj: com.vektorraum.aviatorsbot.generated.metar.Data_source, __scope: scala.xml.NamespaceBinding): scala.xml.MetaData = {
      var attr: scala.xml.MetaData  = scala.xml.Null
      __obj.attributes.toList map {
        case ("@name", _) => __obj.name foreach { x => attr = scala.xml.Attribute(null, "name", x.toString, attr) }
        case (key, x) => attr = scala.xml.Attribute((x.namespace map { __scope.getPrefix(_) }).orNull, x.key.orNull, x.value.toString, attr)
      }
      attr
    }

    def writesChildNodes(__obj: com.vektorraum.aviatorsbot.generated.metar.Data_source, __scope: scala.xml.NamespaceBinding): Seq[scala.xml.Node] =
      Nil

  }

  trait DefaultComvektorraumaviatorsbotgenerated_RequestFormat extends scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.Request] with scalaxb.CanWriteChildNodes[com.vektorraum.aviatorsbot.generated.metar.Request] {
    val targetNamespace: Option[String] = None
    import scalaxb.ElemName._
    
    def reads(seq: scala.xml.NodeSeq, stack: List[scalaxb.ElemName]): Either[String, com.vektorraum.aviatorsbot.generated.metar.Request] = seq match {
      case node: scala.xml.Node => Right(com.vektorraum.aviatorsbot.generated.metar.Request(scala.collection.immutable.ListMap(List(
        (node \ "@type").headOption map { x => scalaxb.DataRecord(x, node, scalaxb.fromXML[String](x, scalaxb.ElemName(node) :: stack)) } map { "@type" -> _ }
        ).flatten[(String, scalaxb.DataRecord[Any])]: _*)))
      case _ => Left("reads failed: seq must be scala.xml.Node")
    }
    
    override def writesAttribute(__obj: com.vektorraum.aviatorsbot.generated.metar.Request, __scope: scala.xml.NamespaceBinding): scala.xml.MetaData = {
      var attr: scala.xml.MetaData  = scala.xml.Null
      __obj.attributes.toList map {
        case ("@type", _) => __obj.typeValue foreach { x => attr = scala.xml.Attribute(null, "type", x.toString, attr) }
        case (key, x) => attr = scala.xml.Attribute((x.namespace map { __scope.getPrefix(_) }).orNull, x.key.orNull, x.value.toString, attr)
      }
      attr
    }

    def writesChildNodes(__obj: com.vektorraum.aviatorsbot.generated.metar.Request, __scope: scala.xml.NamespaceBinding): Seq[scala.xml.Node] =
      Nil

  }

  trait DefaultComvektorraumaviatorsbotgenerated_ErrorsFormat extends scalaxb.ElemNameParser[com.vektorraum.aviatorsbot.generated.metar.Errors] {
    val targetNamespace: Option[String] = None
    
    def parser(node: scala.xml.Node, stack: List[scalaxb.ElemName]): Parser[com.vektorraum.aviatorsbot.generated.metar.Errors] =
      phrase(opt(((scalaxb.ElemName(None, "error"))) ^^ 
        { case p1 => com.vektorraum.aviatorsbot.generated.metar.ErrorsSequence1(scalaxb.fromXML[String](p1, scalaxb.ElemName(node) :: stack)) }) ^^
      { case p1 =>
      com.vektorraum.aviatorsbot.generated.metar.Errors(p1) })
    
    def writesChildNodes(__obj: com.vektorraum.aviatorsbot.generated.metar.Errors, __scope: scala.xml.NamespaceBinding): Seq[scala.xml.Node] =
      (__obj.errorssequence1 map { scalaxb.toXML[com.vektorraum.aviatorsbot.generated.metar.ErrorsSequence1](_, None, Some("errorssequence1"), __scope, false) } getOrElse {Nil})
  }

  trait DefaultComvektorraumaviatorsbotgenerated_ErrorsSequence1Format extends scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.ErrorsSequence1] {
    def reads(seq: scala.xml.NodeSeq, stack: List[scalaxb.ElemName]): Either[String, com.vektorraum.aviatorsbot.generated.metar.ErrorsSequence1] = Left("don't call me.")
    
    def writes(__obj: com.vektorraum.aviatorsbot.generated.metar.ErrorsSequence1, __namespace: Option[String], __elementLabel: Option[String], 
        __scope: scala.xml.NamespaceBinding, __typeAttribute: Boolean): scala.xml.NodeSeq =
      scalaxb.toXML[String](__obj.error, None, Some("error"), __scope, false)

  }
  trait DefaultComvektorraumaviatorsbotgenerated_WarningsFormat extends scalaxb.ElemNameParser[com.vektorraum.aviatorsbot.generated.metar.Warnings] {
    val targetNamespace: Option[String] = None
    
    def parser(node: scala.xml.Node, stack: List[scalaxb.ElemName]): Parser[com.vektorraum.aviatorsbot.generated.metar.Warnings] =
      phrase(opt(((scalaxb.ElemName(None, "warning"))) ^^ 
        { case p1 => com.vektorraum.aviatorsbot.generated.metar.WarningsSequence1(scalaxb.fromXML[String](p1, scalaxb.ElemName(node) :: stack)) }) ^^
      { case p1 =>
      com.vektorraum.aviatorsbot.generated.metar.Warnings(p1) })
    
    def writesChildNodes(__obj: com.vektorraum.aviatorsbot.generated.metar.Warnings, __scope: scala.xml.NamespaceBinding): Seq[scala.xml.Node] =
      (__obj.warningssequence1 map { scalaxb.toXML[com.vektorraum.aviatorsbot.generated.metar.WarningsSequence1](_, None, Some("warningssequence1"), __scope, false) } getOrElse {Nil})
  }

  trait DefaultComvektorraumaviatorsbotgenerated_WarningsSequence1Format extends scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.WarningsSequence1] {
    def reads(seq: scala.xml.NodeSeq, stack: List[scalaxb.ElemName]): Either[String, com.vektorraum.aviatorsbot.generated.metar.WarningsSequence1] = Left("don't call me.")
    
    def writes(__obj: com.vektorraum.aviatorsbot.generated.metar.WarningsSequence1, __namespace: Option[String], __elementLabel: Option[String], 
        __scope: scala.xml.NamespaceBinding, __typeAttribute: Boolean): scala.xml.NodeSeq =
      scalaxb.toXML[String](__obj.warning, None, Some("warning"), __scope, false)

  }
  trait DefaultComvektorraumaviatorsbotgenerated_DataFormat extends scalaxb.ElemNameParser[com.vektorraum.aviatorsbot.generated.metar.Data] {
    val targetNamespace: Option[String] = None
    
    def parser(node: scala.xml.Node, stack: List[scalaxb.ElemName]): Parser[com.vektorraum.aviatorsbot.generated.metar.Data] =
      phrase(safeRep(((scalaxb.ElemName(None, "METAR"))) ^^ 
        { case p1 => com.vektorraum.aviatorsbot.generated.metar.DataSequence1(scalaxb.fromXML[com.vektorraum.aviatorsbot.generated.metar.METAR](p1, scalaxb.ElemName(node) :: stack)) }) ^^
      { case p1 =>
      com.vektorraum.aviatorsbot.generated.metar.Data(p1,
        scala.collection.immutable.ListMap(List(
        (node \ "@num_results").headOption map { x => scalaxb.DataRecord(x, node, scalaxb.fromXML[Int](x, scalaxb.ElemName(node) :: stack)) } map { "@num_results" -> _ }
        ).flatten[(String, scalaxb.DataRecord[Any])]: _*)) })
    
    override def writesAttribute(__obj: com.vektorraum.aviatorsbot.generated.metar.Data, __scope: scala.xml.NamespaceBinding): scala.xml.MetaData = {
      var attr: scala.xml.MetaData  = scala.xml.Null
      __obj.attributes.toList map {
        case ("@num_results", _) => __obj.num_results foreach { x => attr = scala.xml.Attribute(null, "num_results", x.toString, attr) }
        case (key, x) => attr = scala.xml.Attribute((x.namespace map { __scope.getPrefix(_) }).orNull, x.key.orNull, x.value.toString, attr)
      }
      attr
    }

    def writesChildNodes(__obj: com.vektorraum.aviatorsbot.generated.metar.Data, __scope: scala.xml.NamespaceBinding): Seq[scala.xml.Node] =
      (__obj.datasequence1 flatMap { scalaxb.toXML[com.vektorraum.aviatorsbot.generated.metar.DataSequence1](_, None, Some("datasequence1"), __scope, false) })
  }

  trait DefaultComvektorraumaviatorsbotgenerated_DataSequence1Format extends scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.DataSequence1] {
    def reads(seq: scala.xml.NodeSeq, stack: List[scalaxb.ElemName]): Either[String, com.vektorraum.aviatorsbot.generated.metar.DataSequence1] = Left("don't call me.")
    
    def writes(__obj: com.vektorraum.aviatorsbot.generated.metar.DataSequence1, __namespace: Option[String], __elementLabel: Option[String], 
        __scope: scala.xml.NamespaceBinding, __typeAttribute: Boolean): scala.xml.NodeSeq =
      scalaxb.toXML[com.vektorraum.aviatorsbot.generated.metar.METAR](__obj.METAR, None, Some("METAR"), __scope, false)

  }
  trait DefaultComvektorraumaviatorsbotgenerated_METARFormat extends scalaxb.ElemNameParser[com.vektorraum.aviatorsbot.generated.metar.METAR] {
    val targetNamespace: Option[String] = None
    
    def parser(node: scala.xml.Node, stack: List[scalaxb.ElemName]): Parser[com.vektorraum.aviatorsbot.generated.metar.METAR] =
      phrase(opt(scalaxb.ElemName(None, "raw_text")) ~ 
      opt(scalaxb.ElemName(None, "station_id")) ~ 
      opt(scalaxb.ElemName(None, "observation_time")) ~ 
      opt(scalaxb.ElemName(None, "latitude")) ~ 
      opt(scalaxb.ElemName(None, "longitude")) ~ 
      opt(scalaxb.ElemName(None, "temp_c")) ~ 
      opt(scalaxb.ElemName(None, "dewpoint_c")) ~ 
      opt(scalaxb.ElemName(None, "wind_dir_degrees")) ~ 
      opt(scalaxb.ElemName(None, "wind_speed_kt")) ~ 
      opt(scalaxb.ElemName(None, "wind_gust_kt")) ~ 
      opt(scalaxb.ElemName(None, "visibility_statute_mi")) ~ 
      opt(scalaxb.ElemName(None, "altim_in_hg")) ~ 
      opt(scalaxb.ElemName(None, "sea_level_pressure_mb")) ~ 
      opt(scalaxb.ElemName(None, "quality_control_flags")) ~ 
      opt(scalaxb.ElemName(None, "wx_string")) ~ 
      safeRep(scalaxb.ElemName(None, "sky_condition")) ~ 
      opt(scalaxb.ElemName(None, "flight_category")) ~ 
      opt(scalaxb.ElemName(None, "three_hr_pressure_tendency_mb")) ~ 
      opt(scalaxb.ElemName(None, "maxT_c")) ~ 
      opt(scalaxb.ElemName(None, "minT_c")) ~ 
      opt(scalaxb.ElemName(None, "maxT24hr_c")) ~ 
      opt(scalaxb.ElemName(None, "minT24hr_c")) ~ 
      opt(scalaxb.ElemName(None, "precip_in")) ~ 
      opt(scalaxb.ElemName(None, "pcp3hr_in")) ~ 
      opt(scalaxb.ElemName(None, "pcp6hr_in")) ~ 
      opt(scalaxb.ElemName(None, "pcp24hr_in")) ~ 
      opt(scalaxb.ElemName(None, "snow_in")) ~ 
      opt(scalaxb.ElemName(None, "vert_vis_ft")) ~ 
      opt(scalaxb.ElemName(None, "metar_type")) ~ 
      opt(scalaxb.ElemName(None, "elevation_m")) ^^
      { case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 ~ p9 ~ p10 ~ p11 ~ p12 ~ p13 ~ p14 ~ p15 ~ p16 ~ p17 ~ p18 ~ p19 ~ p20 ~ p21 ~ p22 ~ p23 ~ p24 ~ p25 ~ p26 ~ p27 ~ p28 ~ p29 ~ p30 =>
      com.vektorraum.aviatorsbot.generated.metar.METAR(p1.headOption map { scalaxb.fromXML[String](_, scalaxb.ElemName(node) :: stack) },
        p2.headOption map { scalaxb.fromXML[String](_, scalaxb.ElemName(node) :: stack) },
        p3.headOption map { scalaxb.fromXML[String](_, scalaxb.ElemName(node) :: stack) },
        p4.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p5.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p6.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p7.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p8.headOption map { scalaxb.fromXML[Int](_, scalaxb.ElemName(node) :: stack) },
        p9.headOption map { scalaxb.fromXML[Int](_, scalaxb.ElemName(node) :: stack) },
        p10.headOption map { scalaxb.fromXML[Int](_, scalaxb.ElemName(node) :: stack) },
        p11.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p12.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p13.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p14.headOption map { scalaxb.fromXML[com.vektorraum.aviatorsbot.generated.metar.Quality_control_flags](_, scalaxb.ElemName(node) :: stack) },
        p15.headOption map { scalaxb.fromXML[String](_, scalaxb.ElemName(node) :: stack) },
        p16 map { scalaxb.fromXML[com.vektorraum.aviatorsbot.generated.metar.Sky_condition](_, scalaxb.ElemName(node) :: stack) },
        p17.headOption map { scalaxb.fromXML[String](_, scalaxb.ElemName(node) :: stack) },
        p18.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p19.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p20.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p21.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p22.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p23.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p24.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p25.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p26.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p27.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) },
        p28.headOption map { scalaxb.fromXML[Int](_, scalaxb.ElemName(node) :: stack) },
        p29.headOption map { scalaxb.fromXML[String](_, scalaxb.ElemName(node) :: stack) },
        p30.headOption map { scalaxb.fromXML[Float](_, scalaxb.ElemName(node) :: stack) }) })
    
    def writesChildNodes(__obj: com.vektorraum.aviatorsbot.generated.metar.METAR, __scope: scala.xml.NamespaceBinding): Seq[scala.xml.Node] =
      Seq.concat(__obj.raw_text map { scalaxb.toXML[String](_, None, Some("raw_text"), __scope, false) } getOrElse {Nil},
        __obj.station_id map { scalaxb.toXML[String](_, None, Some("station_id"), __scope, false) } getOrElse {Nil},
        __obj.observation_time map { scalaxb.toXML[String](_, None, Some("observation_time"), __scope, false) } getOrElse {Nil},
        __obj.latitude map { scalaxb.toXML[Float](_, None, Some("latitude"), __scope, false) } getOrElse {Nil},
        __obj.longitude map { scalaxb.toXML[Float](_, None, Some("longitude"), __scope, false) } getOrElse {Nil},
        __obj.temp_c map { scalaxb.toXML[Float](_, None, Some("temp_c"), __scope, false) } getOrElse {Nil},
        __obj.dewpoint_c map { scalaxb.toXML[Float](_, None, Some("dewpoint_c"), __scope, false) } getOrElse {Nil},
        __obj.wind_dir_degrees map { scalaxb.toXML[Int](_, None, Some("wind_dir_degrees"), __scope, false) } getOrElse {Nil},
        __obj.wind_speed_kt map { scalaxb.toXML[Int](_, None, Some("wind_speed_kt"), __scope, false) } getOrElse {Nil},
        __obj.wind_gust_kt map { scalaxb.toXML[Int](_, None, Some("wind_gust_kt"), __scope, false) } getOrElse {Nil},
        __obj.visibility_statute_mi map { scalaxb.toXML[Float](_, None, Some("visibility_statute_mi"), __scope, false) } getOrElse {Nil},
        __obj.altim_in_hg map { scalaxb.toXML[Float](_, None, Some("altim_in_hg"), __scope, false) } getOrElse {Nil},
        __obj.sea_level_pressure_mb map { scalaxb.toXML[Float](_, None, Some("sea_level_pressure_mb"), __scope, false) } getOrElse {Nil},
        __obj.quality_control_flags map { scalaxb.toXML[com.vektorraum.aviatorsbot.generated.metar.Quality_control_flags](_, None, Some("quality_control_flags"), __scope, false) } getOrElse {Nil},
        __obj.wx_string map { scalaxb.toXML[String](_, None, Some("wx_string"), __scope, false) } getOrElse {Nil},
        __obj.sky_condition flatMap { scalaxb.toXML[com.vektorraum.aviatorsbot.generated.metar.Sky_condition](_, None, Some("sky_condition"), __scope, false) },
        __obj.flight_category map { scalaxb.toXML[String](_, None, Some("flight_category"), __scope, false) } getOrElse {Nil},
        __obj.three_hr_pressure_tendency_mb map { scalaxb.toXML[Float](_, None, Some("three_hr_pressure_tendency_mb"), __scope, false) } getOrElse {Nil},
        __obj.maxT_c map { scalaxb.toXML[Float](_, None, Some("maxT_c"), __scope, false) } getOrElse {Nil},
        __obj.minT_c map { scalaxb.toXML[Float](_, None, Some("minT_c"), __scope, false) } getOrElse {Nil},
        __obj.maxT24hr_c map { scalaxb.toXML[Float](_, None, Some("maxT24hr_c"), __scope, false) } getOrElse {Nil},
        __obj.minT24hr_c map { scalaxb.toXML[Float](_, None, Some("minT24hr_c"), __scope, false) } getOrElse {Nil},
        __obj.precip_in map { scalaxb.toXML[Float](_, None, Some("precip_in"), __scope, false) } getOrElse {Nil},
        __obj.pcp3hr_in map { scalaxb.toXML[Float](_, None, Some("pcp3hr_in"), __scope, false) } getOrElse {Nil},
        __obj.pcp6hr_in map { scalaxb.toXML[Float](_, None, Some("pcp6hr_in"), __scope, false) } getOrElse {Nil},
        __obj.pcp24hr_in map { scalaxb.toXML[Float](_, None, Some("pcp24hr_in"), __scope, false) } getOrElse {Nil},
        __obj.snow_in map { scalaxb.toXML[Float](_, None, Some("snow_in"), __scope, false) } getOrElse {Nil},
        __obj.vert_vis_ft map { scalaxb.toXML[Int](_, None, Some("vert_vis_ft"), __scope, false) } getOrElse {Nil},
        __obj.metar_type map { scalaxb.toXML[String](_, None, Some("metar_type"), __scope, false) } getOrElse {Nil},
        __obj.elevation_m map { scalaxb.toXML[Float](_, None, Some("elevation_m"), __scope, false) } getOrElse {Nil})

  }

  trait DefaultComvektorraumaviatorsbotgenerated_Quality_control_flagsFormat extends scalaxb.ElemNameParser[com.vektorraum.aviatorsbot.generated.metar.Quality_control_flags] {
    val targetNamespace: Option[String] = None
    
    def parser(node: scala.xml.Node, stack: List[scalaxb.ElemName]): Parser[com.vektorraum.aviatorsbot.generated.metar.Quality_control_flags] =
      phrase(opt(scalaxb.ElemName(None, "corrected")) ~ 
      opt(scalaxb.ElemName(None, "auto")) ~ 
      opt(scalaxb.ElemName(None, "auto_station")) ~ 
      opt(scalaxb.ElemName(None, "maintenance_indicator_on")) ~ 
      opt(scalaxb.ElemName(None, "no_signal")) ~ 
      opt(scalaxb.ElemName(None, "lightning_sensor_off")) ~ 
      opt(scalaxb.ElemName(None, "freezing_rain_sensor_off")) ~ 
      opt(scalaxb.ElemName(None, "present_weather_sensor_off")) ^^
      { case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 =>
      com.vektorraum.aviatorsbot.generated.metar.Quality_control_flags(p1.headOption map { scalaxb.fromXML[String](_, scalaxb.ElemName(node) :: stack) },
        p2.headOption map { scalaxb.fromXML[String](_, scalaxb.ElemName(node) :: stack) },
        p3.headOption map { scalaxb.fromXML[String](_, scalaxb.ElemName(node) :: stack) },
        p4.headOption map { scalaxb.fromXML[String](_, scalaxb.ElemName(node) :: stack) },
        p5.headOption map { scalaxb.fromXML[String](_, scalaxb.ElemName(node) :: stack) },
        p6.headOption map { scalaxb.fromXML[String](_, scalaxb.ElemName(node) :: stack) },
        p7.headOption map { scalaxb.fromXML[String](_, scalaxb.ElemName(node) :: stack) },
        p8.headOption map { scalaxb.fromXML[String](_, scalaxb.ElemName(node) :: stack) }) })
    
    def writesChildNodes(__obj: com.vektorraum.aviatorsbot.generated.metar.Quality_control_flags, __scope: scala.xml.NamespaceBinding): Seq[scala.xml.Node] =
      Seq.concat(__obj.corrected map { scalaxb.toXML[String](_, None, Some("corrected"), __scope, false) } getOrElse {Nil},
        __obj.auto map { scalaxb.toXML[String](_, None, Some("auto"), __scope, false) } getOrElse {Nil},
        __obj.auto_station map { scalaxb.toXML[String](_, None, Some("auto_station"), __scope, false) } getOrElse {Nil},
        __obj.maintenance_indicator_on map { scalaxb.toXML[String](_, None, Some("maintenance_indicator_on"), __scope, false) } getOrElse {Nil},
        __obj.no_signal map { scalaxb.toXML[String](_, None, Some("no_signal"), __scope, false) } getOrElse {Nil},
        __obj.lightning_sensor_off map { scalaxb.toXML[String](_, None, Some("lightning_sensor_off"), __scope, false) } getOrElse {Nil},
        __obj.freezing_rain_sensor_off map { scalaxb.toXML[String](_, None, Some("freezing_rain_sensor_off"), __scope, false) } getOrElse {Nil},
        __obj.present_weather_sensor_off map { scalaxb.toXML[String](_, None, Some("present_weather_sensor_off"), __scope, false) } getOrElse {Nil})

  }

  trait DefaultComvektorraumaviatorsbotgenerated_Sky_conditionFormat extends scalaxb.XMLFormat[com.vektorraum.aviatorsbot.generated.metar.Sky_condition] with scalaxb.CanWriteChildNodes[com.vektorraum.aviatorsbot.generated.metar.Sky_condition] {
    val targetNamespace: Option[String] = None
    import scalaxb.ElemName._
    
    def reads(seq: scala.xml.NodeSeq, stack: List[scalaxb.ElemName]): Either[String, com.vektorraum.aviatorsbot.generated.metar.Sky_condition] = seq match {
      case node: scala.xml.Node => Right(com.vektorraum.aviatorsbot.generated.metar.Sky_condition(scala.collection.immutable.ListMap(List(
        (node \ "@sky_cover").headOption map { x => scalaxb.DataRecord(x, node, scalaxb.fromXML[String](x, scalaxb.ElemName(node) :: stack)) } map { "@sky_cover" -> _ },
        (node \ "@cloud_base_ft_agl").headOption map { x => scalaxb.DataRecord(x, node, scalaxb.fromXML[Int](x, scalaxb.ElemName(node) :: stack)) } map { "@cloud_base_ft_agl" -> _ }
        ).flatten[(String, scalaxb.DataRecord[Any])]: _*)))
      case _ => Left("reads failed: seq must be scala.xml.Node")
    }
    
    override def writesAttribute(__obj: com.vektorraum.aviatorsbot.generated.metar.Sky_condition, __scope: scala.xml.NamespaceBinding): scala.xml.MetaData = {
      var attr: scala.xml.MetaData  = scala.xml.Null
      __obj.attributes.toList map {
        case ("@sky_cover", _) => __obj.sky_cover foreach { x => attr = scala.xml.Attribute(null, "sky_cover", x.toString, attr) }
        case ("@cloud_base_ft_agl", _) => __obj.cloud_base_ft_agl foreach { x => attr = scala.xml.Attribute(null, "cloud_base_ft_agl", x.toString, attr) }
        case (key, x) => attr = scala.xml.Attribute((x.namespace map { __scope.getPrefix(_) }).orNull, x.key.orNull, x.value.toString, attr)
      }
      attr
    }

    def writesChildNodes(__obj: com.vektorraum.aviatorsbot.generated.metar.Sky_condition, __scope: scala.xml.NamespaceBinding): Seq[scala.xml.Node] =
      Nil

  }


}
