import scalaj.http.Http

class HttpService {
  def request(url: String, headers: Seq[(String, String)]): String =
    Http(url)
      .headers(headers)
      .asString
      .body
}
