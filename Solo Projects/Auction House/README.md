#Project 4: Distributed Auction House

The Distributed Auction House is a multiple-application project that 
utilizes remote connectivity and concurrency to implement several distinct 
actors:

1. The Bank
2. Auction Houses
3. Agents

These are listed as actors specifically because each of them, and the 
majority of their subcomponents, are modeled after the Actor model, where a 
process or thread receives data, processes that data, and sends that data, 
protecting its own data and processes by disallowing external modification 
of itself. This was used to enable a large number of connections to servers 
to place requests that an actor could process sequentially, avoiding issues 
that often arise with concurrency. 

To this end, each major component has several key capabilites:
1. The ability to spin off connection threads to maintain communication with 
   a diverse range of clients.
2. A queue of messages, implemented in a thread-safe way, to ensure the 
   atomicity of those messages.
3. A message-processing method, called by a thread that actively monitors 
   the queue, which takes messages and makes decisions about actions based 
   on the message received. 
   
These are the key components. While other utility methods exist, most of 
these could reside inside one of these three components with no change in 
functionality. Even delayed actions are forced to adhere to this schema, 
with a BidTimer class needing to queue a message to fulfill its mandate of 
existence. 

After this main structure was established, the bulk of the work involved was 
in developing the message processing and message-sending.

Bank, AuctionHouse, and Agent are all application-ready classes.

Bank takes the desired port number as a command-line argument and sets up a 
server to monitor for connections from Agents and AuctionHouses. It prints 
out its public IP address and port information. It also has a DEBUG flag set 
that prints diagnostic information to STDOUT. 

AuctionHouse takes the Bank's address and port number as command-line 
arguments. It establishes a connection and registers for a bank account and 
account number with the bank. It sets up its own server and provides this 
information to the Bank, which stores it for dissemination to Agents.

Agent also takes the Bank's address and port number as command-line 
arguments. Once it has connected and registered with the bank, the bank 
provides a series of communications detailing the AuctionHouses available 
for bidding. The Agent established concurrently threaded connections with 
each AuctionHouse, which provide it with data on items for auction. Agent 
uses a separate thread for reading and writing to the user to enable the 
updating of information as it is received from connections without 
interrupting control-selection flow of input. 

At time of turn-in, communication over external networks was non-functional, 
due to issues with CS machine access and personal router port-forwarding 
access. The remainder of requirements are functional and robust. 