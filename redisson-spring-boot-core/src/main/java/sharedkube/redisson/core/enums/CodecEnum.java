package sharedkube.redisson.core.enums;

import org.redisson.client.codec.ByteArrayCodec;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.CborJacksonCodec;
import org.redisson.codec.FstCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.KryoCodec;
import org.redisson.codec.LZ4Codec;
import org.redisson.codec.MsgPackJacksonCodec;
import org.redisson.codec.SerializationCodec;
import org.redisson.codec.SmileJacksonCodec;
import org.redisson.codec.SnappyCodec;

/**
 * @author huahouye@gmail.com
 * @date 2020/08/15
 */
public enum CodecEnum {

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

    KRYO_CODEC {
        @Override
        public Codec getInstance() {
            return new KryoCodec();
        }
    },

    SERIALIZATION_CODEC {
        @Override
        public Codec getInstance() {
            return new SerializationCodec();
        }
    },

    FST_CODEC {
        @Override
        public Codec getInstance() {
            return new FstCodec();
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

    STRING_CODEC {
        @Override
        public Codec getInstance() {
            return new StringCodec();
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