// Import stylesheets
import { Base64 } from 'js-base64';

export class Base64Converter {
  static b64EncodeUnicode(str: any) {
    // first we use encodeURIComponent to get percent-encoded UTF-8,
    // then we convert the percent encodings into raw bytes which
    // can be fed into btoa.
    return btoa(
      encodeURIComponent(str).replace(
        /%([0-9A-F]{2})/g,
        // function toSolidBytes(match, p1) {
        (match, p1) => {
          // console.debug('match: ' + match);
          return String.fromCharCode(('0x' + p1) as any);
        }
      )
    );
  }

  static b64DecodeUnicode(str) {
    // Going backwards: from bytestream, to percent-encoding, to original string.
    return decodeURIComponent(
      atob(str)
        .split('')
        .map(function (c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        })
        .join('')
    );
  }
}

let str1 = Base64Converter.b64EncodeUnicode('✓ à la mode');
// "4pyTIMOgIGxhIG1vZGU="
console.log(str1);

let str2 = Base64Converter.b64EncodeUnicode('Bob\nFred');
// "Qm9iCkZyZWQ="
console.log(str2);

let str3 = Base64Converter.b64DecodeUnicode('4pyTIMOgIGxhIG1vZGU=');
let str3_1 = Base64Converter.b64DecodeUnicode(str1);
// "✓ à la mode"
console.log(str3);
console.log(str3_1);

let str4 = Base64Converter.b64DecodeUnicode('Qm9iCkZyZWQ=');
let str4_1 = Base64Converter.b64DecodeUnicode(str2);
// "Bob\nFred"
console.log(str4);
console.log(str4_1);

// Using library
console.log('=== === ===');
let strL1 = Base64.encode('✓ à la mode');
// "4pyTIMOgIGxhIG1vZGU="
console.log(strL1);
console.log(Base64.decode(strL1));

let strL2 = Base64.encode('Bob\nFred');
// "Qm9iCkZyZWQ="
console.log(strL2);
console.log(Base64.decode(strL2));

