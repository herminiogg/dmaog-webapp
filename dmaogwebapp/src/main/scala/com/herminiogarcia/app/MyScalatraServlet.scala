package com.herminiogarcia.app

import java.net.URL
import java.io.File
import java.io.PrintWriter
import com.herminiogarcia.dmaog.codeGeneration.CodeGenerator
import org.zeroturnaround.zip.ZipUtil
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

import scala.io.Source
import scala.util.{Failure, Success, Try}

class MyScalatraServlet extends ScalatraServlet with CorsSupport with JacksonJsonSupport {

  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  options("/*") {
    response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"))
  }

  get("/") {
    views.html.hello()
  }

  post("/generateCodeWithDMAOG") {
    val content = parsedBody.extract[DMAOGCodeGeneration]
    val conversionType = content.conversionType
    val rdf = content.rdf
    val shexml = content.shexml
    val tmpDirPath = "tmp"

    val tmpDir = new File(tmpDirPath)
    tmpDir.listFiles().foreach(_.delete())
    tmpDir.delete()
    tmpDir.mkdir()

    if(conversionType == "ShExML") {
      new CodeGenerator(Some(shexml), "ShExML", tmpDirPath, "com.example",
        None, None, None,
        None, None, None, None, 
        None, false).generate()
    } else if(conversionType == "RML") {
      new CodeGenerator(Some(rdf), "RML", tmpDirPath, "com.example",
        None, None, None,
        None, None, None, None, 
        None, false).generate()
    } else {
      val pw = new PrintWriter(tmpDirPath + "/data.ttl")
      pw.write(rdf)
      pw.close()

      val datafile = tmpDirPath + "/data.ttl"

      new CodeGenerator(None, "", tmpDirPath, "com.example",
        None, None, None,
        None, None, None, None, 
        Some(datafile), false).generate()
    }

    val zipFilePath = "results.zip"
    new File(zipFilePath).createNewFile()
    ZipUtil.pack(new File("tmp"), new File(zipFilePath))

    contentType = "application/octet-stream"
    val file = new File(zipFilePath)
    response.setHeader("Content-Disposition", "attachment; filename=" + file.getName)
    file
  }

}

case class DMAOGCodeGeneration(conversionType: String, shexml: String, rdf: String)
