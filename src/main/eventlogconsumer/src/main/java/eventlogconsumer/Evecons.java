package eventlogconsumer;

import io.confluent.kafka.serializers.NonRecordContainer;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DatumWriter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.path;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.sink.SinkRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import io.confluent.connect.avro.AvroData;
import io.confluent.connect.hdfs.RecordWriter;
import io.confluent.connect.RecordWriterProvider;

public class AvroRecordWriterProvider implements RecordWriterProvider{
	 private static final Logger log = LoggerFactory.getLogger(AvroRecordWriterProvider.class);
	  private final static String EXTENSION = ".avro";

	  @Override
	  public String getExtension() {
	    return EXTENSION;
	  }
	  
	  @Override
	  public RecordWriter<SinkRecord> getRecordWriter(Configuration conf,final String fileName,SinkRecord record,final AvroData avrodata) throws IOException{
		  DatumWriter<Object> datumWriter = new GenericDatumWriter<>();
		  final DataFileWriter<Object> writer = new DataFileWriter<>(datumWriter);
		  Path path = new Path(fileName);
		  
		  final Schema schema record.valueSchema();
		  final FSDataOutputStream out = path.getFileSysytem(conf).create(path);
		  org.apache.avro.Schema avroSchema = avroData.fromConnectSchema(schema);
		  writer.create(avroSchema,out);
		  
		  return new RecordWriter<SinkRecord>(){
			 @Override
			 public void write(SinkRecord record)throws IOException{
				 log.trace("sink record:{}",record.toString());
				 Object value = avroData.fromConnectData(schema,record.value());
				 
				 if (value instanceof NonRecordContainer)
			          writer.append(((NonRecordContainer) value).getValue());
			        else
			          writer.append(value);
			 }
			 @Override
		      public void close() throws IOException {
		        writer.close();
		      }
		    };
		  }
	  }






public class Evecons {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
