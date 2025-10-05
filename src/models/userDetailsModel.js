import mongoose from 'mongoose';

const userSchema = new mongoose.Schema({
  username: {
    type: String,
    required: [true, 'Username is required.'],
    minlength: [3, 'Username must be at least 3 characters long.'],
    maxlength: [30, 'Username cannot exceed 30 characters.'],
    unique: true,
    trim: true,
  },
  height: {
    type: String,
    required: [true, 'Height is required.'],
    match: [/^\d+(cm|CM|Cm)$/, 'Height must be in the format: e.g., 170cm'],
  },
  weight: {
    type: String,
    required: [true, 'Weight is required.'],
    match: [/^\d+(kg|KG|Kg)$/, 'Weight must be in the format: e.g., 70kg'],
  },
  age: {
    type: Number,
    required: [true, 'Age is required.'],
    min: [10, 'Age must be at least 10.'],
    max: [120, 'Age cannot exceed 120.'],
  },
  goal: {
    type: String,
    required: [true, 'Goal is required.'],
  },
}, { timestamps: true });



userSchema.statics.createOrUpdate = async function (data) {
  const { username, ...rest } = data;

  const updated = await this.findOneAndUpdate(
    { username },
    { $set: rest },
    { new: true, upsert: true, runValidators: true }
  );

  return updated;
};

const UserDetails = mongoose.model('UserDetails', userSchema);
export default UserDetails;
