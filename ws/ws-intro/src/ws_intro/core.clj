(ns ws-intro.core
  (:require [clojure.data.json :as json]
            [clojure.string :as s])
  (:import [org.webbitserver WebServer WebServers WebSocketHandler]
           [org.webbitserver.handler StaticFileHandler]))

(import java.util.LinkedList)

(def connections (java.util.LinkedList.))

(defn on-open [connection]
  (.add connections connection)
  (println "Opened new connection: " connection))

(defn on-close [connection]
  (println "Closed connection: " connection)
  (.remove connections connection))

(defn on-message [connection jsonmsg]
  (let [message (-> jsonmsg json/read-json (get-in [:data :message]))]
    (doseq [aConnection (seq connections)] (.send aConnection (json/json-str {:type "broadcast" :message message })))))

(defn -main []
  (doto (WebServers/createWebServer 8080)
    (.add "/websocket"
          (proxy [WebSocketHandler] []
            (onOpen [c] (on-open c))
            (onClose [c] (on-close c))
            (onMessage [c j] (on-message c j))))
    (.add (StaticFileHandler. "."))
    (.start)))