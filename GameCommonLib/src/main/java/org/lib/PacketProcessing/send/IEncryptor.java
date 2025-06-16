package org.lib.PacketProcessing.send;

public interface IEncryptor<T>
{
    byte[] encrypt(T message);
}
