package filesync;

import filesync.BlockUnavailableException;
import filesync.CopyBlockInstruction;
import filesync.Instruction;
import filesync.InstructionFactory;
import filesync.NewBlockInstruction;
import filesync.SynchronisedFile;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 */

public class SyncClientThread implements Runnable {

//	/**
//	 * Variables necesarias para la sincronización
//	 */
        private static DataInputStream in;
	private static DataOutputStream out;
        private static Socket clientSocket;
	private static String host;
	private static String filename;
	private static String action;
	private static int blockSize;
	private static SynchronisedFile file;
        private static String reply;
        
//	/**
//	 * Declarar variables necesarias para la comunicación con 
//	 * el servidor
//	 * 
//	 */
//		
	SyncClientThread(String h, String fn, String a, int bs){
		host = h;
		filename = fn;
		action = a;
		blockSize = bs;
                try {
                in = new DataInputStream(in);
                out= new DataOutputStream(out);
                clientSocket = new Socket(host,7800);
                   } catch (Exception e) {
            }
	}
//	
//	@Override
	public void run() {
		try {
			in= new DataInputStream(clientSocket.getInputStream());
                        out = new DataOutputStream(clientSocket.getOutputStream());
                        
                        System.out.println("Nombre del archivo enviado: "+filename);
                        out.writeUTF(filename);
                    
                        
                        switch(action){
                      case "commit":
                        actAsSender();
                          break;
                      case "update":
                        actAsReceiver(reply);
                          break;
                      default:
                        clientSocket.close();
                        
                    }
			
			/**
			 * Instanciar objetos necesarios para leer y escribir 
			 * en el stream
			 */

          /**
            * Empieza enviando el nombre del archivo que desea sincronizar
           */
                     System.out.println("Enviando el nombre del archivo que se desea sincronizar");
                     out.writeUTF(filename);
                     
                        
          /*
			 * El siguiente código hace que el cliente espere por un acuso de 
           * recibo por parte del servidor. Esto debe hacerse siempre despues
           * de enviar un mensaje.  
			*/ 
                     String acuse ="";
                     
                     System.out.println("Recibiendo acuso de recibo");
                     in.readUTF(); // el acuso de recibo
                     reply= in.readUTF();
                     System.out.println("Acuse de recibo: " +reply);
                     
                     
                     
            while( !reply.equals( "OK" ) ) {
		try {
                    System.out.println("Waiting server to accept the filename...");
                    } catch (Exception e) {
                        System.out.println("Could not receive filename confirmation from server: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
                }
			  }
			  System.out.println("Confirmed.");
			 

  
			/**
			 * Enviar mensaje conteniendo la accion que se va 
			 * a realizar
			 * "commit" o "update"*/
                          
                          
			System.out.println( "Sending action: " + action );
			 
			
                        // Esperar por el acuso de recibo
			

			/**
			 * Enviar el tamaño del bloque especificado: blocksize
			 */
			  System.out.println( "Sending blockSize: " + blockSize );
			
			
                        // Esperar por el acuso de recibo

			/*
			 * Initialise the SynchronisedFiles.
			 */
			file = new SynchronisedFile( filename, blockSize );
			
			switch( action ){
				case "commit":
					actAsSender();
					break;
				case "update":
                                         // el acuso de recibo
                                         reply= in.readUTF();
					actAsReceiver(reply);
					break;
				default:
					System.out.println( "Invalid action. Usage: java -jar syncclient.jar hostname filename (commit | update) blocksize" );
					System.exit(-1);
			}	
		} catch (IOException e) {
//			System.out.println(ex.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private static void actAsSender() {
                Instruction inst;
		long startTime = System.currentTimeMillis();
		try {
			System.out.println("SyncClient: calling fromFile.CheckFileState()");
			file.CheckFileState();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		
		while( (inst = file.NextInstruction()) != null ){
			
			/**
			 * El cliente envia las instrucciones de sincronización 
			 * hacia el servidor
			 * 
			 * Los mensajes deben ser empaquetados utilizando el 
			 * método ToJSON() dentro de la clase Instruction
			 */
                    inst.ToJSON();
                    try {
			 	String msg="";
                                inst.FromJSON(msg);
				System.out.println( "Sending: " + msg );
                                System.out.println( "Received: "+ reply );
             
				/** Si el servidor envia como respuesta "NEW", quiere
				 * decir que existe un cambio en el archivo y por lo
				 * tanto el cliente debe cambiar el CopyBlock por un 
				 * NewBlock
				 */ 			 
				if( reply.equals( "NEW" ) ) {
					/*
					 * El cliente cambia la instrucción CopyBlock por 
					 * una NewBlock y lo envia.
					 * El mensaje debe ser empaquetado antes de 
					 * enviarse.
					 */
                                        String msg2 ="";
					Instruction upgraded = new NewBlockInstruction( ( CopyBlockInstruction ) inst );
                                        msg2 = upgraded.ToJSON();
					inst.FromJSON(msg2);                                        					
					/**
					 * Enviar la nueva instrucción al servidor 
					 * y recibir el acuso de recibo
					 */ 
					 System.err.println( "Sending: " + msg2 );
				}                         
			} catch (Exception e) {
				System.out.println("Socket:"+e.getMessage());
				e.printStackTrace();
				System.exit(-1);
			} 
			/**
			 * Verificar que el acuso de recibo es OK y moverse a 
			 * la siguiente instrucción
			 */
			while( !reply.equals( "OK" ) ) {
			    try {
                    System.out.println("Waiting server to accept the filename...");
                    } catch (Exception e) {
                        System.out.println("Could not receive filename confirmation from server: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
                }
			}
			System.out.println("OK received. Move to the next instruction.");
                        if( inst.Type().equals("EndUpdate")  ) {
			System.out.println("Sync finalised.");
			long finishTime = System.currentTimeMillis();
			System.out.println("Total time of Synchrohisation: " + (finishTime - startTime));
			System.exit(0);
			}
		}
	
            
            
//		Instruction inst;
//		String reply = "";
//		long startTime = System.currentTimeMillis();
//		try {
//			System.out.println("SyncClient: calling fromFile.CheckFileState()");
//			file.CheckFileState();
//		} catch (IOException e) {
//			System.out.println(ex.getMessage());
//			e.printStackTrace();
//			System.exit(-1);
//		} catch (InterruptedException e) {
//			System.out.println(ex.getMessage());
//			e.printStackTrace();
//			System.exit(-1);
//		}
//		
//		while( (inst = file.NextInstruction()) != null ){
//			
//			/**
//			 * El cliente envia las instrucciones de sincronización 
//			 * hacia el servidor
//			 * 
//			 * Los mensajes deben ser empaquetados utilizando el 
//			 * método ToJSON() dentro de la clase Instruction
//			 */
//			 try {
//			 	String msg="";
//					System.out.println( "Sending: " + msg );
//			  
//			  
//					System.out.println( "Received: "+ reply );
//             
//				/** Si el servidor envia como respuesta "NEW", quiere
//				 * decir que existe un cambio en el archivo y por lo
//				 * tanto el cliente debe cambiar el CopyBlock por un 
//				 * NewBlock
//				 */ 
//			 
//				if( reply.equals( "NEW" ) ) {
//					/*
//					 * El cliente cambia la instrucción CopyBlock por 
//					 * una NewBlock y lo envia.
//					 * El mensaje debe ser empaquetado antes de 
//					 * enviarse.
//					 */
//					Instruction upgraded = new NewBlockInstruction( ( CopyBlockInstruction ) inst );
//					
//					
//					/**
//					 * Enviar la nueva instrucción al servidor 
//					 * y recibir el acuso de recibo
//					 */ 
//					 System.err.println( "Sending: " + msg2 );
//				}
//			} catch (UnknownHostException e) {
//				System.out.println("Socket:"+e.getMessage());
//				e.printStackTrace();
//				System.exit(-1);
//			} catch (EOFException e){
//				System.out.println("EOF:"+e.getMessage());
//				e.printStackTrace();
//				System.exit(-1);
//			} catch (IOException e){
//				System.out.println("readline: " + e.getMessage());
//				e.printStackTrace();
//				System.exit(-1);
//			}
//			
//
//			/**
//			 * Verificar que el acuso de recibo es OK y moverse a 
//			 * la siguiente instrucción
//			 */
//			while( !reply.equals( "OK" ) ) {
//				...
//			}
//			 System.out.println("OK received. Move to the next instruction.");
//
//			finalise sync
//			if( inst.Type().equals("EndUpdate")  ) {
//				System.out.println("Sync finalised.");
//				long finishTime = System.currentTimeMillis();
//				System.out.println("Total time of Synchrohisation: " + (finishTime - startTime));
//				System.exit(0);
//			}
//		}
	}
	
	private static void actAsReceiver(String msg) {
		long startTime = System.currentTimeMillis();
		while(true) {
			try{
				/**
				 * La acción es "update" por lo tanto es el cliente
				 * quien recibirá los datos desde el servidor
				 */
                                 reply = in.readUTF();
				 System.out.println("Client reading data" +reply);
                                 
                                 
				
				/*
				 * El ciente recibe la instrucción aqui, la cual
				 * debe ser desempaquetada antes de ser procesada.
				 * Utilizar el metodo fromJSON de la clase
				 * InstrucionFactory
				 */
				InstructionFactory instFact = new InstructionFactory();
				Instruction receivedInst = instFact.FromJSON(reply);

				try {
					// The client processes the instruction
					file.ProcessInstruction( receivedInst );
				} catch ( IOException e ) {
					System.out.println( e.getMessage() );
					e.printStackTrace();
					System.exit(-1); // just die at the first sign of trouble
				} catch ( BlockUnavailableException e ) {
					// The client does not have the bytes referred to
					// by the block hash.
					try {
						/**
						 * Si se lanza esta excepción quiere decir que
						 * el cliente no tiene los bytes a los que 
						 * hace referencia el bloque hash recibido.
						 * Por lo tanto el cliente debe enviar una 
						 * petición al servidor para que le sean 
						 * enviados los bytes reales contenidos en el
						 * bloque.
						 */
						 System.out.println( "Cliente ide bytes reales. Bloque no encontrado" );
                                                 out.writeUTF("NEW");


						/*
						 * El cliente recibe el nuevo bloque de bytes
						 * los cuales deben ser desempaquetados antes
						 * de ser procesados.
						 * Utilizar el metodo fromJSON de la clase
						 * InstructionFactory
						 */
						 System.out.println("Cliente espera recibir nuevo bloque");
                                                 String newBlock = in.readUTF();
                                                 
                                                
						
						Instruction receivedInst2 =  instFact.FromJSON(newBlock);
                                                
						file.ProcessInstruction( receivedInst2 );
					} catch (IOException e1) {
//						System.out.println( e1.getMessage() );
						e.printStackTrace();
						System.exit(-1);
					} catch (BlockUnavailableException e1) {
						assert(false); // a NewBlockInstruction can never throw this exception
					}
				}
				/*
				 * Como estamos usando un protocolo 
				 * peticion-respuesta, el cliente debe enviar un
				 * acuso de recibo al servidor para indicar que 
				 * el bloque fue recibido correctamente y que la 
				 * siguiente instrucción puede ser enviada.
				 */
				System.out.println( "Cliente envia acusodel recibo" );
                                out.writeUTF("OK");
				
				//finalise sync
				if( receivedInst.Type().equals("EndUpdate")  ) {
					System.out.println("Sync finalised.");
					long finishTime = System.currentTimeMillis();
					System.out.println("Total time of Synchrohisation: " + (finishTime - startTime));
					System.exit(0);
				}
			}catch (EOFException e){
				System.out.println( "EOF: " + e.getMessage() );
				e.printStackTrace();
				System.exit(-1);
			} catch(IOException e) {
				System.out.println( "readline: " + e.getMessage() );
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
}
