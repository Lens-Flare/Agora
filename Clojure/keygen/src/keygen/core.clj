(ns keygen.core
  (:import [java.lang RuntimeException]
           [java.security Key KeyPair KeyPairGenerator]))

(defn serialize
  "Serializes an object to a byte array"
  [obj path]
  (with-open [ostream (new java.io.ObjectOutputStream (new java.io.FileOutputStream path))]
    (. ostream writeObject obj)))

(defn deserialize
  "Deserializes an object from a byte array"
  [path]
  (with-open [istream (new java.io.ObjectInputStream (new java.io.FileInputStream path))]
    (. istream readObject)))

(defn -main [bits path]
  (when-not
    (contains? #{512 1024 2048 4096} bits)
    (throw (new RuntimeException (str "Invalid number of bits: " bits))))
  (let [kpg (KeyPairGenerator/getInstance "RSA")
        _ (. kpg initialize bits)
        kp (. kpg genKeyPair)
        pub (. kp getPublic)
        priv (. kp getPrivate)]
    (serialize priv path)
    (serialize pub (str path ".pub"))))