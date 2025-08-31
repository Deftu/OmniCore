package dev.deftu.omnicore.common

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.util.ByteProcessor
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.GatheringByteChannel
import java.nio.channels.ScatteringByteChannel
import java.nio.charset.Charset

//#if MC >= 1.12.2
import dev.deftu.omnicore.api.annotations.VersionedAbove
import java.nio.channels.FileChannel
//#endif

public data class OmniPacketReceiverContext(
    public val channel: Identifier,
    public val buffer: PacketByteBuf
) : ByteBuf() {

    override fun capacity(): Int {
        return this.buffer.capacity()
    }

    override fun capacity(newCapacity: Int): ByteBuf {
        return this.buffer.capacity(newCapacity)
    }

    override fun maxCapacity(): Int {
        return this.buffer.maxCapacity()
    }

    override fun alloc(): ByteBufAllocator {
        return this.buffer.alloc()
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun order(): ByteOrder {
        return this.buffer.order()
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun order(order: ByteOrder): ByteBuf {
        return this.buffer.order(order)
    }

    override fun unwrap(): ByteBuf {
        return this.buffer.unwrap()
    }

    override fun isDirect(): Boolean {
        return this.buffer.isDirect
    }

    override fun readerIndex(): Int {
        return this.buffer.readerIndex()
    }

    override fun readerIndex(i: Int): ByteBuf {
        return this.buffer.readerIndex(i)
    }

    override fun writerIndex(): Int {
        return this.buffer.writerIndex()
    }

    override fun writerIndex(i: Int): ByteBuf {
        return this.buffer.writerIndex(i)
    }

    override fun setIndex(i: Int, j: Int): ByteBuf {
        return this.buffer.setIndex(i, j)
    }

    override fun readableBytes(): Int {
        return this.buffer.readableBytes()
    }

    override fun writableBytes(): Int {
        return this.buffer.writableBytes()
    }

    override fun maxWritableBytes(): Int {
        return this.buffer.maxWritableBytes()
    }

    override fun isReadable(): Boolean {
        return this.buffer.isReadable
    }

    override fun isReadable(i: Int): Boolean {
        return this.buffer.isReadable(i)
    }

    override fun isWritable(): Boolean {
        return this.buffer.isWritable
    }

    override fun isWritable(i: Int): Boolean {
        return this.buffer.isWritable(i)
    }

    override fun clear(): ByteBuf {
        return this.buffer.clear()
    }

    override fun markReaderIndex(): ByteBuf {
        return this.buffer.markReaderIndex()
    }

    override fun resetReaderIndex(): ByteBuf {
        return this.buffer.resetReaderIndex()
    }

    override fun markWriterIndex(): ByteBuf {
        return this.buffer.markWriterIndex()
    }

    override fun resetWriterIndex(): ByteBuf {
        return this.buffer.resetWriterIndex()
    }

    override fun discardReadBytes(): ByteBuf {
        return this.buffer.discardReadBytes()
    }

    override fun discardSomeReadBytes(): ByteBuf {
        return this.buffer.discardSomeReadBytes()
    }

    override fun ensureWritable(i: Int): ByteBuf {
        return this.buffer.ensureWritable(i)
    }

    override fun ensureWritable(i: Int, bl: Boolean): Int {
        return this.buffer.ensureWritable(i, bl)
    }

    override fun getBoolean(i: Int): Boolean {
        return this.buffer.getBoolean(i)
    }

    override fun getByte(i: Int): Byte {
        return this.buffer.getByte(i)
    }

    override fun getUnsignedByte(i: Int): Short {
        return this.buffer.getUnsignedByte(i)
    }

    override fun getShort(i: Int): Short {
        return this.buffer.getShort(i)
    }

    override fun getUnsignedShort(i: Int): Int {
        return this.buffer.getUnsignedShort(i)
    }

    override fun getMedium(i: Int): Int {
        return this.buffer.getMedium(i)
    }

    override fun getUnsignedMedium(i: Int): Int {
        return this.buffer.getUnsignedMedium(i)
    }

    override fun getInt(i: Int): Int {
        return this.buffer.getInt(i)
    }

    override fun getUnsignedInt(i: Int): Long {
        return this.buffer.getUnsignedInt(i)
    }

    override fun getLong(i: Int): Long {
        return this.buffer.getLong(i)
    }

    override fun getChar(i: Int): Char {
        return this.buffer.getChar(i)
    }

    override fun getFloat(i: Int): Float {
        return this.buffer.getFloat(i)
    }

    override fun getDouble(i: Int): Double {
        return this.buffer.getDouble(i)
    }

    override fun getBytes(i: Int, byteBuf: ByteBuf): ByteBuf {
        return this.buffer.getBytes(i, byteBuf)
    }

    override fun getBytes(i: Int, byteBuf: ByteBuf, j: Int): ByteBuf {
        return this.buffer.getBytes(i, byteBuf, j)
    }

    override fun getBytes(i: Int, byteBuf: ByteBuf, j: Int, k: Int): ByteBuf {
        return this.buffer.getBytes(i, byteBuf, j, k)
    }

    override fun getBytes(i: Int, bs: ByteArray): ByteBuf {
        return this.buffer.getBytes(i, bs)
    }

    override fun getBytes(i: Int, bs: ByteArray, j: Int, k: Int): ByteBuf {
        return this.buffer.getBytes(i, bs, j, k)
    }

    override fun getBytes(i: Int, byteBuffer: ByteBuffer): ByteBuf {
        return this.buffer.getBytes(i, byteBuffer)
    }

    @Throws(IOException::class)
    override fun getBytes(i: Int, outputStream: OutputStream, j: Int): ByteBuf {
        return this.buffer.getBytes(i, outputStream, j)
    }

    @Throws(IOException::class)
    override fun getBytes(i: Int, gatheringByteChannel: GatheringByteChannel, j: Int): Int {
        return this.buffer.getBytes(i, gatheringByteChannel, j)
    }

    override fun setBoolean(i: Int, bl: Boolean): ByteBuf {
        return this.buffer.setBoolean(i, bl)
    }

    override fun setByte(i: Int, j: Int): ByteBuf {
        return this.buffer.setByte(i, j)
    }

    override fun setShort(i: Int, j: Int): ByteBuf {
        return this.buffer.setShort(i, j)
    }

    override fun setMedium(i: Int, j: Int): ByteBuf {
        return this.buffer.setMedium(i, j)
    }

    override fun setInt(i: Int, j: Int): ByteBuf {
        return this.buffer.setInt(i, j)
    }

    override fun setLong(i: Int, l: Long): ByteBuf {
        return this.buffer.setLong(i, l)
    }

    override fun setChar(i: Int, j: Int): ByteBuf {
        return this.buffer.setChar(i, j)
    }

    override fun setFloat(i: Int, f: Float): ByteBuf {
        return this.buffer.setFloat(i, f)
    }

    override fun setDouble(i: Int, d: Double): ByteBuf {
        return this.buffer.setDouble(i, d)
    }

    override fun setBytes(i: Int, byteBuf: ByteBuf): ByteBuf {
        return this.buffer.setBytes(i, byteBuf)
    }

    override fun setBytes(i: Int, byteBuf: ByteBuf, j: Int): ByteBuf {
        return this.buffer.setBytes(i, byteBuf, j)
    }

    override fun setBytes(i: Int, byteBuf: ByteBuf, j: Int, k: Int): ByteBuf {
        return this.buffer.setBytes(i, byteBuf, j, k)
    }

    override fun setBytes(i: Int, bs: ByteArray): ByteBuf {
        return this.buffer.setBytes(i, bs)
    }

    override fun setBytes(i: Int, bs: ByteArray, j: Int, k: Int): ByteBuf {
        return this.buffer.setBytes(i, bs, j, k)
    }

    override fun setBytes(i: Int, byteBuffer: ByteBuffer): ByteBuf {
        return this.buffer.setBytes(i, byteBuffer)
    }

    @Throws(IOException::class)
    override fun setBytes(i: Int, inputStream: InputStream, j: Int): Int {
        return this.buffer.setBytes(i, inputStream, j)
    }

    @Throws(IOException::class)
    override fun setBytes(i: Int, scatteringByteChannel: ScatteringByteChannel, j: Int): Int {
        return this.buffer.setBytes(i, scatteringByteChannel, j)
    }

    override fun setZero(i: Int, j: Int): ByteBuf {
        return this.buffer.setZero(i, j)
    }

    override fun readBoolean(): Boolean {
        return this.buffer.readBoolean()
    }

    override fun readByte(): Byte {
        return this.buffer.readByte()
    }

    override fun readUnsignedByte(): Short {
        return this.buffer.readUnsignedByte()
    }

    override fun readShort(): Short {
        return this.buffer.readShort()
    }

    override fun readUnsignedShort(): Int {
        return this.buffer.readUnsignedShort()
    }

    override fun readMedium(): Int {
        return this.buffer.readMedium()
    }

    override fun readUnsignedMedium(): Int {
        return this.buffer.readUnsignedMedium()
    }

    override fun readInt(): Int {
        return this.buffer.readInt()
    }

    override fun readUnsignedInt(): Long {
        return this.buffer.readUnsignedInt()
    }

    override fun readLong(): Long {
        return this.buffer.readLong()
    }

    override fun readChar(): Char {
        return this.buffer.readChar()
    }

    override fun readFloat(): Float {
        return this.buffer.readFloat()
    }

    override fun readDouble(): Double {
        return this.buffer.readDouble()
    }

    override fun readBytes(i: Int): ByteBuf {
        return this.buffer.readBytes(i)
    }

    override fun readSlice(i: Int): ByteBuf {
        return this.buffer.readSlice(i)
    }

    override fun readBytes(byteBuf: ByteBuf): ByteBuf {
        return this.buffer.readBytes(byteBuf)
    }

    override fun readBytes(byteBuf: ByteBuf, i: Int): ByteBuf {
        return this.buffer.readBytes(byteBuf, i)
    }

    override fun readBytes(byteBuf: ByteBuf, i: Int, j: Int): ByteBuf {
        return this.buffer.readBytes(byteBuf, i, j)
    }

    override fun readBytes(bs: ByteArray): ByteBuf {
        return this.buffer.readBytes(bs)
    }

    override fun readBytes(bs: ByteArray, i: Int, j: Int): ByteBuf {
        return this.buffer.readBytes(bs, i, j)
    }

    override fun readBytes(byteBuffer: ByteBuffer): ByteBuf {
        return this.buffer.readBytes(byteBuffer)
    }

    @Throws(IOException::class)
    override fun readBytes(outputStream: OutputStream, i: Int): ByteBuf {
        return this.buffer.readBytes(outputStream, i)
    }

    @Throws(IOException::class)
    override fun readBytes(gatheringByteChannel: GatheringByteChannel, i: Int): Int {
        return this.buffer.readBytes(gatheringByteChannel, i)
    }

    override fun skipBytes(i: Int): ByteBuf {
        return this.buffer.skipBytes(i)
    }

    override fun writeBoolean(bl: Boolean): ByteBuf {
        return this.buffer.writeBoolean(bl)
    }

    override fun writeByte(i: Int): ByteBuf {
        return this.buffer.writeByte(i)
    }

    override fun writeShort(i: Int): ByteBuf {
        return this.buffer.writeShort(i)
    }

    override fun writeMedium(i: Int): ByteBuf {
        return this.buffer.writeMedium(i)
    }

    override fun writeInt(i: Int): ByteBuf {
        return this.buffer.writeInt(i)
    }

    override fun writeLong(l: Long): ByteBuf {
        return this.buffer.writeLong(l)
    }

    override fun writeChar(i: Int): ByteBuf {
        return this.buffer.writeChar(i)
    }

    override fun writeFloat(f: Float): ByteBuf {
        return this.buffer.writeFloat(f)
    }

    override fun writeDouble(d: Double): ByteBuf {
        return this.buffer.writeDouble(d)
    }

    override fun writeBytes(byteBuf: ByteBuf): ByteBuf {
        return this.buffer.writeBytes(byteBuf)
    }

    override fun writeBytes(byteBuf: ByteBuf, i: Int): ByteBuf {
        return this.buffer.writeBytes(byteBuf, i)
    }

    override fun writeBytes(byteBuf: ByteBuf, i: Int, j: Int): ByteBuf {
        return this.buffer.writeBytes(byteBuf, i, j)
    }

    override fun writeBytes(bs: ByteArray): ByteBuf {
        return this.buffer.writeBytes(bs)
    }

    override fun writeBytes(bs: ByteArray, i: Int, j: Int): ByteBuf {
        return this.buffer.writeBytes(bs, i, j)
    }

    override fun writeBytes(byteBuffer: ByteBuffer): ByteBuf {
        return this.buffer.writeBytes(byteBuffer)
    }

    @Throws(IOException::class)
    override fun writeBytes(inputStream: InputStream, i: Int): Int {
        return this.buffer.writeBytes(inputStream, i)
    }

    @Throws(IOException::class)
    override fun writeBytes(scatteringByteChannel: ScatteringByteChannel, i: Int): Int {
        return this.buffer.writeBytes(scatteringByteChannel, i)
    }

    override fun writeZero(i: Int): ByteBuf {
        return this.buffer.writeZero(i)
    }

    override fun indexOf(i: Int, j: Int, b: Byte): Int {
        return this.buffer.indexOf(i, j, b)
    }

    override fun bytesBefore(b: Byte): Int {
        return this.buffer.bytesBefore(b)
    }

    override fun bytesBefore(i: Int, b: Byte): Int {
        return this.buffer.bytesBefore(i, b)
    }

    override fun bytesBefore(i: Int, j: Int, b: Byte): Int {
        return this.buffer.bytesBefore(i, j, b)
    }

    override fun forEachByte(byteProcessor: ByteProcessor): Int {
        return this.buffer.forEachByte(byteProcessor)
    }

    override fun forEachByte(i: Int, j: Int, byteProcessor: ByteProcessor): Int {
        return this.buffer.forEachByte(i, j, byteProcessor)
    }

    override fun forEachByteDesc(byteProcessor: ByteProcessor): Int {
        return this.buffer.forEachByteDesc(byteProcessor)
    }

    override fun forEachByteDesc(i: Int, j: Int, byteProcessor: ByteProcessor): Int {
        return this.buffer.forEachByteDesc(i, j, byteProcessor)
    }

    override fun copy(): ByteBuf {
        return this.buffer.copy()
    }

    override fun copy(i: Int, j: Int): ByteBuf {
        return this.buffer.copy(i, j)
    }

    override fun slice(): ByteBuf {
        return this.buffer.slice()
    }

    override fun slice(i: Int, j: Int): ByteBuf {
        return this.buffer.slice(i, j)
    }

    override fun duplicate(): ByteBuf {
        return this.buffer.duplicate()
    }

    override fun nioBufferCount(): Int {
        return this.buffer.nioBufferCount()
    }

    override fun nioBuffer(): ByteBuffer {
        return this.buffer.nioBuffer()
    }

    override fun nioBuffer(i: Int, j: Int): ByteBuffer {
        return this.buffer.nioBuffer(i, j)
    }

    override fun internalNioBuffer(i: Int, j: Int): ByteBuffer {
        return this.buffer.internalNioBuffer(i, j)
    }

    override fun nioBuffers(): Array<ByteBuffer> {
        return this.buffer.nioBuffers()
    }

    override fun nioBuffers(i: Int, j: Int): Array<ByteBuffer> {
        return this.buffer.nioBuffers(i, j)
    }

    override fun hasArray(): Boolean {
        return this.buffer.hasArray()
    }

    override fun array(): ByteArray {
        return this.buffer.array()
    }

    override fun arrayOffset(): Int {
        return this.buffer.arrayOffset()
    }

    override fun hasMemoryAddress(): Boolean {
        return this.buffer.hasMemoryAddress()
    }

    override fun memoryAddress(): Long {
        return this.buffer.memoryAddress()
    }

    override fun toString(charset: Charset): String {
        return this.buffer.toString(charset)
    }

    override fun toString(i: Int, j: Int, charset: Charset): String {
        return this.buffer.toString(i, j, charset)
    }

    override fun hashCode(): Int {
        return this.buffer.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return this.buffer == other
    }

    override fun compareTo(other: ByteBuf): Int {
        return this.buffer.compareTo(other)
    }

    override fun toString(): String {
        return this.buffer.toString()
    }

    override fun retain(i: Int): ByteBuf {
        return this.buffer.retain(i)
    }

    override fun retain(): ByteBuf {
        return this.buffer.retain()
    }

    override fun refCnt(): Int {
        return this.buffer.refCnt()
    }

    override fun release(): Boolean {
        return this.buffer.release()
    }

    override fun release(i: Int): Boolean {
        return this.buffer.release(i)
    }

    //#if MC >= 1.12.2
    @VersionedAbove("1.12.2")
    override fun isReadOnly(): Boolean {
        return this.buffer.isReadOnly
    }

    @VersionedAbove("1.12.2")
    override fun asReadOnly(): ByteBuf {
        return this.buffer.asReadOnly()
    }

    @VersionedAbove("1.12.2")
    override fun getShortLE(i: Int): Short {
        return this.buffer.getShortLE(i)
    }

    @VersionedAbove("1.12.2")
    override fun getUnsignedShortLE(i: Int): Int {
        return this.buffer.getUnsignedShortLE(i)
    }

    @VersionedAbove("1.12.2")
    override fun getMediumLE(i: Int): Int {
        return this.buffer.getMediumLE(i)
    }

    @VersionedAbove("1.12.2")
    override fun getUnsignedMediumLE(i: Int): Int {
        return this.buffer.getUnsignedMediumLE(i)
    }

    @VersionedAbove("1.12.2")
    override fun getIntLE(i: Int): Int {
        return this.buffer.getIntLE(i)
    }

    @VersionedAbove("1.12.2")
    override fun getUnsignedIntLE(i: Int): Long {
        return this.buffer.getUnsignedIntLE(i)
    }

    @VersionedAbove("1.12.2")
    override fun getLongLE(i: Int): Long {
        return this.buffer.getLongLE(i)
    }

    @VersionedAbove("1.12.2")
    @Throws(IOException::class)
    override fun getBytes(i: Int, fileChannel: FileChannel, l: Long, j: Int): Int {
        return this.buffer.getBytes(i, fileChannel, l, j)
    }

    @VersionedAbove("1.12.2")
    override fun getCharSequence(i: Int, j: Int, charset: Charset): CharSequence {
        return this.buffer.getCharSequence(i, j, charset)
    }

    @VersionedAbove("1.12.2")
    override fun setShortLE(i: Int, j: Int): ByteBuf {
        return this.buffer.setShortLE(i, j)
    }

    @VersionedAbove("1.12.2")
    override fun setMediumLE(i: Int, j: Int): ByteBuf {
        return this.buffer.setMediumLE(i, j)
    }

    @VersionedAbove("1.12.2")
    override fun setIntLE(i: Int, j: Int): ByteBuf {
        return this.buffer.setIntLE(i, j)
    }

    @VersionedAbove("1.12.2")
    override fun setLongLE(i: Int, l: Long): ByteBuf {
        return this.buffer.setLongLE(i, l)
    }

    @VersionedAbove("1.12.2")
    @Throws(IOException::class)
    override fun setBytes(i: Int, fileChannel: FileChannel, l: Long, j: Int): Int {
        return this.buffer.setBytes(i, fileChannel, l, j)
    }

    @VersionedAbove("1.12.2")
    override fun setCharSequence(i: Int, charSequence: CharSequence, charset: Charset): Int {
        return this.buffer.setCharSequence(i, charSequence, charset)
    }

    @VersionedAbove("1.12.2")
    override fun readShortLE(): Short {
        return this.buffer.readShortLE()
    }

    @VersionedAbove("1.12.2")
    override fun readUnsignedShortLE(): Int {
        return this.buffer.readUnsignedShortLE()
    }

    @VersionedAbove("1.12.2")
    override fun readMediumLE(): Int {
        return this.buffer.readMediumLE()
    }

    @VersionedAbove("1.12.2")
    override fun readUnsignedMediumLE(): Int {
        return this.buffer.readUnsignedMediumLE()
    }

    @VersionedAbove("1.12.2")
    override fun readIntLE(): Int {
        return this.buffer.readIntLE()
    }

    @VersionedAbove("1.12.2")
    override fun readUnsignedIntLE(): Long {
        return this.buffer.readUnsignedIntLE()
    }

    @VersionedAbove("1.12.2")
    override fun readLongLE(): Long {
        return this.buffer.readLongLE()
    }

    @VersionedAbove("1.12.2")
    override fun readRetainedSlice(i: Int): ByteBuf {
        return this.buffer.readRetainedSlice(i)
    }

    @VersionedAbove("1.12.2")
    override fun readCharSequence(i: Int, charset: Charset): CharSequence {
        return this.buffer.readCharSequence(i, charset)
    }

    @VersionedAbove("1.12.2")
    @Throws(IOException::class)
    override fun readBytes(fileChannel: FileChannel, l: Long, i: Int): Int {
        return this.buffer.readBytes(fileChannel, l, i)
    }

    @VersionedAbove("1.12.2")
    override fun writeShortLE(i: Int): ByteBuf {
        return this.buffer.writeShortLE(i)
    }

    @VersionedAbove("1.12.2")
    override fun writeMediumLE(i: Int): ByteBuf {
        return this.buffer.writeMediumLE(i)
    }

    @VersionedAbove("1.12.2")
    override fun writeIntLE(i: Int): ByteBuf {
        return this.buffer.writeIntLE(i)
    }

    @VersionedAbove("1.12.2")
    override fun writeLongLE(l: Long): ByteBuf {
        return this.buffer.writeLongLE(l)
    }

    @VersionedAbove("1.12.2")
    @Throws(IOException::class)
    override fun writeBytes(fileChannel: FileChannel, l: Long, i: Int): Int {
        return this.buffer.writeBytes(fileChannel, l, i)
    }

    @VersionedAbove("1.12.2")
    override fun writeCharSequence(charSequence: CharSequence, charset: Charset): Int {
        return this.buffer.writeCharSequence(charSequence, charset)
    }

    @VersionedAbove("1.12.2")
    override fun retainedSlice(): ByteBuf {
        return this.buffer.retainedSlice()
    }

    @VersionedAbove("1.12.2")
    override fun retainedSlice(i: Int, j: Int): ByteBuf {
        return this.buffer.retainedSlice(i, j)
    }

    @VersionedAbove("1.12.2")
    override fun retainedDuplicate(): ByteBuf {
        return this.buffer.retainedDuplicate()
    }

    @VersionedAbove("1.12.2")
    override fun touch(): ByteBuf {
        return this.buffer.touch()
    }

    @VersionedAbove("1.12.2")
    override fun touch(`object`: Any): ByteBuf {
        return this.buffer.touch(`object`)
    }
    //#endif

}
