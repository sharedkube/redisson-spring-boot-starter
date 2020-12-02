package sharedkube.redisson.core.enums;

import org.redisson.client.codec.ByteArrayCodec;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.LongCodec;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.CborJacksonCodec;
import org.redisson.codec.IonJacksonCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.Kryo5Codec;
import org.redisson.codec.KryoCodec;
import org.redisson.codec.LZ4Codec;
import org.redisson.codec.MarshallingCodec;
import org.redisson.codec.MsgPackJacksonCodec;
import org.redisson.codec.SerializationCodec;
import org.redisson.codec.SmileJacksonCodec;
import org.redisson.codec.SnappyCodec;
import org.redisson.codec.SnappyCodecV2;

/**
 * @author huahouye@gmail.com
 * @date 2020/08/15
 */
public enum CodecEnum {

    // default
    MARSHALLING_CODEC {
        @Override
        public Codec getInstance() {
            return new MarshallingCodec();
        }
    },

    JSON_JACKSON_CODEC {
        @Override
        public Codec getInstance() {
            return new JsonJacksonCodec();
        }
    },

    SMILE_JACKSON_CODEC {
        @Override
        public Codec getInstance() {
            return new SmileJacksonCodec();
        }
    },

    CBOR_JACKSON_CODEC {
        @Override
        public Codec getInstance() {
            return new CborJacksonCodec();
        }
    },

    MSG_PACK_JACKSON_CODEC {
        @Override
        public Codec getInstance() {
            return new MsgPackJacksonCodec();
        }
    },

    ION_JACKSON_CODEC {
        @Override
        public Codec getInstance() {
            return new IonJacksonCodec();
        }
    },

    KRYO_CODEC {
        @Override
        public Codec getInstance() {
            return new KryoCodec();
        }
    },

    KRYO5_CODEC {
        @Override
        public Codec getInstance() {
            return new Kryo5Codec();
        }
    },

    /**
     * JDK Serialization binary codec
     * 
     * @author houyehua
     *
     */
    SERIALIZATION_CODEC {
        @Override
        public Codec getInstance() {
            return new SerializationCodec();
        }
    },

    LZ4_CODEC {
        @Override
        public Codec getInstance() {
            return new LZ4Codec();
        }
    },

    SNAPPY_CODEC {
        @Override
        public Codec getInstance() {
            return new SnappyCodec();
        }
    },

    SNAPPY_CODEC_V2 {
        @Override
        public Codec getInstance() {
            return new SnappyCodecV2();
        }
    },
    STRING_CODEC {
        @Override
        public Codec getInstance() {
            return new StringCodec();
        }
    },

    LONG_CODEC {
        @Override
        public Codec getInstance() {
            return new LongCodec();
        }
    },

    BYTE_ARRAY_CODEC {
        @Override
        public Codec getInstance() {
            return new ByteArrayCodec();
        }
    };

    public abstract Codec getInstance();

}