.\Bin\SprotoParser.exe .
move sproto.bytes SProtoBytes

java  -jar  genProto.jar -opcode SProtoLua/Server/opcodes.lua -proto SProtoLua/Client/Protocol.lua

pause