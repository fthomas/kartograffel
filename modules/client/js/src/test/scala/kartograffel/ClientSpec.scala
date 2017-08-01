package kartograffel

import org.scalatest.FunSuite

class ClientSpec extends FunSuite {
  test("run main") {
    Client.main(Array.empty)
    true
  }
}
