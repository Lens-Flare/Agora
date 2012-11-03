(ns ws-intro.core
  (:require [clojure.data.json :as json]
            [clojure.string :as s])
  (:import [org.webbitserver WebServer WebServers WebSocketHandler]
           [org.webbitserver.handler StaticFileHandler]))

(def connections (agent {}))

(defn broadcast [data]
 (doseq [aConnection @connections]
   (. (val aConnection) send (json/json-str data))))

(defn private-msg [toName fromName message]
  (. (@connections toName) send (json/json-str {:type "private"
                                                :from fromName
                                                :message message})))

(defn on-open [connection]
  (println "Opened new connection: " connection))

(defn on-close [connection]
  (doseq [aConnection @connections]
    (when
      (= connection (val aConnection))
      (send connections dissoc (key aConnection))
      (broadcast {:type "signout"
                  :name (key aConnection)})))
  (println "Closed connection: " connection))

(defn on-message [connection jsonmsg]
  (let [msg (-> jsonmsg json/read-json :data)]
    (condp = (msg :type)
      "signin" (do
                 (send connections assoc (msg :name) connection)
                 (. connection send (json/json-str {:type "users"
                                                    :users (keys @connections)}))
                 (broadcast msg))
      "private" (when
                  (= (@connections (msg :from)) connection)
                  (private-msg (msg :to) (msg :from) (msg :message)))
      "broadcast" (when
                    (= (@connections (msg :from)) connection)
                    (broadcast msg))
      ())))

(defn -main []
  (doto (WebServers/createWebServer 8080)
    (.add "/chat"
          (proxy [WebSocketHandler] []
            (onOpen [c] (on-open c))
            (onClose [c] (on-close c))
            (onMessage [c j] (on-message c j))))
    (.add (StaticFileHandler. "."))
    (.start)))