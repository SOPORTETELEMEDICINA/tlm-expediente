package net.amentum.niomedic.expediente.exception;

public enum ExceptionServiceCode {
   EXPEDIENTE("EXPDTE"),
   NOTIFICACIONES("NOTIF"),
   GROUP("G"),
   FORMAT("FMT"),
   CONTROLES("CTRL"),
   CATALOGOS("CAT");
   private final String valCode;

   ExceptionServiceCode(String valCode) {
      this.valCode = valCode;
   }

   @Override
   public String toString() {
      return this.valCode;
   }
}
