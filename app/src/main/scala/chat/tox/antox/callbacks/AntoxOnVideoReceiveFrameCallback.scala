package chat.tox.antox.callbacks

import android.content.Context
import chat.tox.antox.av.YuvVideoFrame
import chat.tox.antox.data.State
import chat.tox.antox.utils.AntoxLog
import chat.tox.antox.wrapper.CallNumber
import im.tox.tox4j.av.data.{Width, Height}

class AntoxOnVideoReceiveFrameCallback(private var ctx: Context) {

  var width: Option[Width] = None
  var height: Option[Height] = None
  var y: Array[Byte] = _
  var u: Array[Byte] = _
  var v: Array[Byte] = _

  def videoFrameCachedYUV(height: Height, yStride: Int, uStride: Int, vStride: Int): Option[(Array[Byte], Array[Byte], Array[Byte])] = {
    println(s"videoFrameCachedYUV called at ${System.currentTimeMillis()}")
    val width: Width = Width.unsafeFromInt(0)

    if (!this.height.contains(height) || !this.width.contains(width)) {
      println("recreating arrays")
      y = new Array(yStride * height.value)
      u = new Array(uStride * (height.value / 2))
      v = new Array(vStride * (height.value / 2))

      this.width = Some(width)
      this.height = Some(height)
    }

    //Some(y, u, v)
    None
  }

  def videoReceiveFrame(callNumber: CallNumber, frame: YuvVideoFrame)(state: Unit): Unit = {
    AntoxLog.debug(s"video frame received at ${System.currentTimeMillis()} for $callNumber dimensions(${frame.width}, ${frame.height}) yuv: ${frame.y.length} ${frame.u.length} ${frame.v.length}")

    State.callManager.get(callNumber).foreach(_.onVideoFrame(frame))
  }
}
