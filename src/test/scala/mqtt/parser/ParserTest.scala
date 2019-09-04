package mqtt.parser

import org.scalatest.{FunSuite, Matchers}
import Parsers._
import mqtt.utils.Bit
import mqtt.utils.BitImplicits._

class ParserTest extends FunSuite with Matchers {
  val zero: Bit = Bit(false)
  val one: Bit = Bit(true)
  val some: Seq[Bit] = Seq(0, 1, 0, 1, 1)
  
  //Item parser
  test("A item parser should parse a single bit") {
    assert(item().run(Seq(zero)) == List((zero, Seq())))
    assert(item().run(Seq(one)) == List((one, Seq())))
    assert(item().run(some) == List((some.head, some.tail)))
  }
  test("A item parser should not parse nothing") {
    assert(item().run(Seq()) == List())
  }
  
  //Bit parser
  test("A bit parser should parse a single specific bit") {
    assert(bit(zero).run(Seq(zero)) == List((zero, Seq())))
    assert(bit(one).run(Seq(one)) == List((one, Seq())))
    assert(bit(zero).run(some) == List((zero, some.tail)))
  }
  test("A bit parser should not parse a different bit") {
    assert(bit(zero).run(Seq(one)) == List())
  }
  
  //Zero parser
  test("A zero parser should parse a zero") {
    assert(Parsers.zero().run(some) == List((zero, some.tail)))
  }
  test("A zero parser should not parse a one") {
    assert(Parsers.zero().run(Seq(one)) == List())
  }
  
  //One parser
  test("A one parser should parse a one") {
    assert(Parsers.one().run(Seq(one)) == List((one, Seq())))
  }
  test("A one parser should not parse something starting with 0") {
    assert(Parsers.one().run(some) == List())
  }
  
  //Bits parser
  test("A bits parser should parse n bits") {
    assert(bits(some size).run(some) == List((some, Seq())))
    assert(bits(some.size - 1).run(some) == List((some.take(some.size - 1), Seq(some.last))))
  }
  test("A bits parser should not parse more than n bits") {
    assert(bits(some.size + 1).run(some) == List())
  }
  test("A bits parser should not parse nothing") {
    assert(bits(some size).run(Seq()) == List())
  }
}