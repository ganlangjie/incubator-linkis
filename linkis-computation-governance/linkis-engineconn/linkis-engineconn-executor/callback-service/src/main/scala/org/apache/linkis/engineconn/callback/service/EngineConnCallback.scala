/*
 * Copyright 2019 WeBank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.linkis.engineconn.callback.service

import org.apache.linkis.common.ServiceInstance
import org.apache.linkis.common.utils.Logging
import org.apache.linkis.protocol.message.RequestProtocol
import org.apache.linkis.rpc.Sender
import org.apache.linkis.manager.common.entity.enumeration.NodeStatus
import org.apache.linkis.manager.common.protocol.engine.EngineConnStatusCallback

trait EngineConnCallback {

  protected def getEMSender: Sender

  def callback(): Unit

}

abstract class AbstractEngineConnStartUpCallback(emInstance: ServiceInstance) extends EngineConnCallback with Logging{

  override protected def getEMSender: Sender = {
    Sender.getSender(emInstance)
  }


  def callback(protocol: RequestProtocol): Unit = {
    protocol match {
      case protocol: EngineConnStatusCallback => {
        if(protocol.status.equals(NodeStatus.Failed)){
          error(s"protocol will send to em: ${protocol}")
        }else{
          info(s"protocol will send to em: ${protocol}")
        }
      }
      case _=>
    }
    getEMSender.send(protocol)
  }
}

