(ns ^{:doc "Common encoding and decoding values"
      :author "Paula Gearon"}
    asami.durable.codec
    (:import [java.nio.charset Charset]))


(def ^:const byte-mask 0xFF)
(def ^:const data-mask 0x0FFFFFFFFFFFFFFF)
(def ^:const sbytes-shift 48)
(def ^:const len-nybble-shift 56)

(def utf8 (Charset/forName "UTF-8"))


