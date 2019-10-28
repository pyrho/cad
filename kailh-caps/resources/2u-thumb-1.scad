union () {
  difference () {
    translate ([-2.85, 0, 0]) {
      union () {
        color ([0, 1, 0, ]) {
          hull () {
            translate ([0, 0, 5]) {
              cube ([1.2, 2.9, 0.01], center=true);
            }
            translate ([0, 0, -3]) {
              translate ([0, 0, 0]) {
                cube ([1.2, 2.9, 0.01], center=true);
              }
            }
          }
        }
        color ([0, 1, 0, ]) {
          hull () {
            translate ([5.7, 0, 5]) {
              cube ([1.2, 2.9, 0.01], center=true);
            }
            translate ([0, 0, -3]) {
              translate ([5.7, 0, 0]) {
                cube ([1.2, 2.9, 0.01], center=true);
              }
            }
          }
        }
      }
    }
    translate ([0, 0, 4.5]) {
      rotate ([0.0,10.000000000000002,0.0]) {
        translate ([0, 0, 24.56125000000001]) {
          scale ([1, 2.1, 1]) {
            sphere ($fn=100, r=26.56125000000001);
          }
        }
      }
    }
  }
  difference () {
    difference () {
      hull () {
        translate ([0, 0, 4.5]) {
          rotate ([0.0,10.000000000000002,0.0]) {
            minkowski () {
              cube ([8.3, 29.0, 0.0099], center=true);
              cylinder ($fs=0.1, h=1.0E-4, r=3, center=true);
            }
          }
        }
        minkowski () {
          cube ([16.5, 34.0, 0.0099], center=true);
          cylinder ($fs=0.1, h=1.0E-4, r=0.5, center=true);
        }
      }
      translate ([0, 0, 4.5]) {
        rotate ([0.0,10.000000000000002,0.0]) {
          translate ([0, 0, 24.56125000000001]) {
            scale ([1, 2.1, 1]) {
              sphere ($fn=100, r=26.56125000000001);
            }
          }
        }
      }
    }
    #union () {
      hull () {
        rotate ([0.0,10.000000000000002,0.0]) {
          translate ([0, 0, 1.0]) {
            minkowski () {
              cube ([6.800000000000001, 27.5, 0.0099], center=true);
              cylinder ($fs=0.1, h=1.0E-4, r=3, center=true);
            }
          }
        }
        translate ([0, 0, -0.001]) {
          minkowski () {
            cube ([15.0, 32.5, 0.0099], center=true);
            cylinder ($fs=0.1, h=1.0E-4, r=0.5, center=true);
          }
        }
      }
    }
  }
}
