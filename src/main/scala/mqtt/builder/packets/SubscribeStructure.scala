package mqtt.builder.packets

import mqtt.builder.Builder
import mqtt.builder.CommonBuilders._
import mqtt.builder.RichBuilder._
import mqtt.model.Packet.Subscribe
import mqtt.model.QoS

/**
  * Provide the structure of Subscribe packet as referred in chapter 3.8.
  */
case object SubscribeStructure extends PacketStructure[Subscribe] {
  
  //3.8.3
  private val topicQoS: Builder[(String, QoS)] = qosBuilder from {
    case (_, qos) => qos
  }
  private val topicFilter: Builder[(String, QoS)] = stringBuilder from {
    case (filter, _) => filter
  }
  
  
  override val builder: Builder[Subscribe] =
    controlPacketType(8) :: zero :: zero :: one :: zero :: remainingLength :: //3.8.1
      packetIdentifier :: //3.8.2
      (topicFilter :: (6 zeros) :: topicQoS).foreach[Subscribe](_.topics) //3.8.3
}
